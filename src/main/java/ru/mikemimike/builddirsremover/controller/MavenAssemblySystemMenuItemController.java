package ru.mikemimike.builddirsremover.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.mikemimike.builddirsremover.config.manager.ConfigManager;
import ru.mikemimike.builddirsremover.config.manager.LanguageChangeListener;
import ru.mikemimike.builddirsremover.config.manager.LanguageManager;
import ru.mikemimike.builddirsremover.properties.BuildDirsRemoverProperties;
import ru.mikemimike.builddirsremover.util.LanguageType;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static ru.mikemimike.builddirsremover.util.AssemblySystemType.MAVEN;

public class MavenAssemblySystemMenuItemController implements LanguageChangeListener {
    private static final Logger log = LogManager.getLogger(MavenAssemblySystemMenuItemController.class.getName());

    @FXML
    private Label mavenAssemblySystemPathToRemoveLabel;

    @FXML
    private Button mavenAssemblySystemPathToRemoveBrowseButton;

    @FXML
    private Button mavenAssemblySystemDeleteButton;

    @FXML
    private TextField mavenAssemblySystemPathToRemove;

    @FXML
    private TextArea mavenLogTextArea;

    @FXML
    public void initialize() {
        BuildDirsRemoverProperties.BuildDirsRemover.Settings settings = ConfigManager.getInstance()
                .getConfig().getBuildDirsRemover().getSettings();

        LanguageManager.getInstance().addLanguageChangeListener(this);
        String language = settings.getLanguage();
        LanguageManager.getInstance().setLanguage(LanguageType.getByName(language));

        String pathToRemove = settings
                .getAssemblySystems().get(MAVEN.getName()).getPathToRemove();

        mavenAssemblySystemPathToRemove.setText(pathToRemove);
        String pathToRemoveText = mavenAssemblySystemPathToRemove.getText();

        if (pathToRemoveText != null && !pathToRemoveText.isEmpty()) {
            mavenAssemblySystemDeleteButton.setDisable(false);
        }

        mavenAssemblySystemPathToRemove.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                mavenAssemblySystemDeleteButton.setDisable(true);
            } else {
                mavenAssemblySystemDeleteButton.setDisable(false);
            }
        });
    }

    public void updateLanguage(LanguageType languageType) {
        Locale locale = Locale.of(languageType.getLocale());
        ResourceBundle bundle = ResourceBundle.getBundle("languages.language", locale);

        mavenAssemblySystemPathToRemoveLabel.setText(bundle.getString("assemblySystemMavenPathToRemoveLabel"));
        mavenAssemblySystemPathToRemoveBrowseButton.setText(bundle.getString("browseButton"));
        mavenAssemblySystemDeleteButton.setText(bundle.getString("deleteFoldersButton"));
    }

    public void handleBrowseMaven() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        String currentPath = mavenAssemblySystemPathToRemove.getText();

        if (currentPath == null) {
            File selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                mavenAssemblySystemPathToRemove.setText(selectedDirectory.getAbsolutePath());
                mavenAssemblySystemDeleteButton.setVisible(true);
            }
        } else {
            File initialDirectory = new File(currentPath);

            if (initialDirectory.exists() && initialDirectory.isDirectory()) {
                directoryChooser.setInitialDirectory(initialDirectory);
            }

            File selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                mavenAssemblySystemPathToRemove.setText(selectedDirectory.getAbsolutePath());
                mavenAssemblySystemDeleteButton.setVisible(true);
            }
        }

    }

    public void handleDelete() {
        Path startPath = Paths.get(mavenAssemblySystemPathToRemove.getText());

        Task<Void> deleteTask = new Task<Void>() {
            private StringBuilder logBuilder = new StringBuilder();
            private int logCount = 0;

            @Override
            protected Void call() {
                try {
                    Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                            if (dir.getFileName().toString().equals("target")) {
                                deleteDirectory(dir);
                                logBuilder.append("Removed folder: ").append(dir).append("\n");
                                logCount++;

                                log.info("Removed folder: {}", dir);

                                //Send logs in TextArea every 10 records
                                if (logCount >= 10) {
                                    Platform.runLater(() -> {
                                        mavenLogTextArea.appendText(logBuilder.toString());
                                        logBuilder.setLength(0);
                                        logCount = 0;
                                    });
                                }
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });

                    // Добавляем оставшиеся логи после завершения
                    if (!logBuilder.isEmpty()) {
                        Platform.runLater(() -> {
                            mavenLogTextArea.appendText(logBuilder.toString());
                        });
                    }
                } catch (IOException e) {
                    log.error("Error when deleting folders: ", e);
                    Platform.runLater(() -> {
                        mavenLogTextArea.appendText("Error when deleting folders: " + e.getMessage() + "\n");
                    });
                }
                return null;
            }
        };

        Thread deleteThread = new Thread(deleteTask);
        deleteThread.setDaemon(true);
        deleteThread.start();
    }


    public void deleteDirectory(Path directory) {
        try(Stream<Path> walk = Files.walk(directory)) {
            if (Files.exists(directory)) {
                walk
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                mavenLogTextArea.appendText("Error when deleting: " + path + " - " + e.getMessage());
                                log.error("Error when deleting: " + path, e);
                            }
                        });
            }
        } catch (Exception e) {
            mavenLogTextArea.appendText("Error when deleting: " + directory + " - " + e.getMessage());
            log.error("Error when deleting: " + directory, e);
        }
    }

    @Override
    public void onLanguageChanged(LanguageType languageType) {
        updateLanguage(languageType);
    }
}
