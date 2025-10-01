package ru.mikemimike.builddirsremover.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import ru.mikemimike.builddirsremover.config.manager.ConfigManager;
import ru.mikemimike.builddirsremover.config.manager.LanguageChangeListener;
import ru.mikemimike.builddirsremover.config.manager.LanguageManager;
import ru.mikemimike.builddirsremover.config.manager.ThemeManager;
import ru.mikemimike.builddirsremover.properties.BuildDirsRemoverProperties;
import ru.mikemimike.builddirsremover.properties.BuildDirsRemoverProperties.BuildDirsRemover.Settings;
import ru.mikemimike.builddirsremover.util.LanguageType;
import ru.mikemimike.builddirsremover.util.ThemeType;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static ru.mikemimike.builddirsremover.util.AssemblySystemType.GRADLE;
import static ru.mikemimike.builddirsremover.util.AssemblySystemType.MAVEN;

public class SettingsController implements LanguageChangeListener {
    @FXML
    private Label settingsMainLabel;

    @FXML
    private Label settingsLanguageLabel;

    @FXML
    private Label settingsThemeLabel;

    @FXML
    private Label settingsAssemblySystemsLabel;

    @FXML
    private Label settingsMavenPathToRemoveLabel;

    @FXML
    private Label settingsMavenDirToRemoveLabel;

    @FXML
    private Label settingsGradlePathToRemoveLabel;

    @FXML
    private Label settingsGradleDirToRemoveLabel;

    @FXML
    private ComboBox<String> settingsLanguageComboBox;

    @FXML
    private ComboBox<String> settingsThemeComboBox;

    @FXML
    private Button settingsMavenPathToRemoveBrowseButton;

    @FXML
    private Button settingsGradlePathToRemoveBrowseButton;

    @FXML
    private TextField settingsMavenPathToRemoveTextField;

    @FXML
    private TextField settingsGradlePathToRemoveTextField;

    @FXML
    private TextField settingsMavenDirToRemoveTextField;

    @FXML
    private TextField settingsGradleDirToRemoveTextField;

    @FXML
    private Button settingsSaveButton;

    @FXML
    public void initialize() {
        Settings settings = ConfigManager.getInstance().getConfig().getBuildDirsRemover().getSettings();
        Map<String, Settings.AssemblySystem> assemblySystems = settings.getAssemblySystems();

        LanguageManager.getInstance().addLanguageChangeListener(this);
        String language = settings.getLanguage();
        LanguageManager.getInstance().setLanguage(LanguageType.getByName(language));

        //LANGUAGE
        settingsLanguageComboBox.setValue(settings.getLanguage());
        settingsLanguageComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            settingsSaveButton.setDisable(false);
        });

        //THEME
        settingsThemeComboBox.setValue(settings.getTheme());
        settingsThemeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            settingsSaveButton.setDisable(false);
        });

        //MAVEN
        settingsMavenDirToRemoveTextField.setText(assemblySystems.get(MAVEN.getName()).getDirToRemove());
        settingsMavenPathToRemoveTextField.setText(assemblySystems.get(MAVEN.getName()).getPathToRemove());
        settingsMavenDirToRemoveTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            settingsSaveButton.setDisable(false);
        });

        settingsMavenPathToRemoveTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            settingsSaveButton.setDisable(false);
        });

        //GRADLE
        settingsGradleDirToRemoveTextField.setText(assemblySystems.get(GRADLE.getName()).getDirToRemove());
        settingsGradlePathToRemoveTextField.setText(assemblySystems.get(GRADLE.getName()).getPathToRemove());
        settingsGradleDirToRemoveTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            settingsSaveButton.setDisable(false);
        });
        settingsGradlePathToRemoveTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            settingsSaveButton.setDisable(false);
        });
    }

    public void updateLanguageComboBox() {
        LanguageType languageType = LanguageType.getByName(settingsLanguageComboBox.getValue());
        LanguageManager.getInstance().setLanguage(languageType);
    }

    public void updateThemeComboBox() {
        String theme = settingsThemeComboBox.getValue();
        Scene scene = settingsThemeComboBox.getScene();

        ThemeManager.getInstance().updateTheme(ThemeType.getByName(theme), scene);
    }

    public void updateLanguage(LanguageType languageType) {
        Locale locale = Locale.of(languageType.getLocale());
        ResourceBundle bundle = ResourceBundle.getBundle("languages.language", locale);

        settingsMainLabel.setText(bundle.getString("settingsMainLabel"));
        settingsLanguageLabel.setText(bundle.getString("settingsLanguageChooseLabel"));
        settingsThemeLabel.setText(bundle.getString("settingsThemeChooseLabel"));
        settingsAssemblySystemsLabel.setText(bundle.getString("settingsAssemblySystemsLabel"));

        settingsMavenPathToRemoveLabel.setText(bundle.getString("settingsPathToRemoveLabel"));
        settingsMavenPathToRemoveBrowseButton.setText(bundle.getString("browseButton"));
        settingsMavenDirToRemoveLabel.setText(bundle.getString("settingsDirToRemoveLabel"));

        settingsGradlePathToRemoveLabel.setText(bundle.getString("settingsPathToRemoveLabel"));
        settingsGradlePathToRemoveBrowseButton.setText(bundle.getString("browseButton"));
        settingsGradleDirToRemoveLabel.setText(bundle.getString("settingsDirToRemoveLabel"));

        settingsSaveButton.setText(bundle.getString("settingsSaveButton"));
    }

    public void handleBrowsePathToRemoveMaven() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(settingsMavenPathToRemoveBrowseButton.getScene().getWindow());
        if (selectedDirectory != null) {
            settingsMavenPathToRemoveTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void handleBrowsePathToRemoveGradle() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(settingsGradlePathToRemoveBrowseButton.getScene().getWindow());
        if (selectedDirectory != null) {
            settingsGradlePathToRemoveTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void handleSaveSettings() {
        BuildDirsRemoverProperties config = ConfigManager.getInstance().getConfig();
        Map<String, Settings.AssemblySystem> assemblySystems = config.getBuildDirsRemover()
                .getSettings()
                .getAssemblySystems();

        //LANGUAGE
        config.getBuildDirsRemover().getSettings().setLanguage(settingsLanguageComboBox.getValue());

        //THEME
        config.getBuildDirsRemover().getSettings().setTheme(settingsThemeComboBox.getValue());

        //GRADLE
        Settings.AssemblySystem gradleAssemblySystem = assemblySystems.get(GRADLE.getName());

        gradleAssemblySystem.setPathToRemove(settingsGradlePathToRemoveTextField.getText());
        assemblySystems.put(GRADLE.getName(), gradleAssemblySystem);

        gradleAssemblySystem.setDirToRemove(settingsGradleDirToRemoveTextField.getText());
        assemblySystems.put(GRADLE.getName(), gradleAssemblySystem);

        //MAVEN
        Settings.AssemblySystem mavenAssemblySystem = assemblySystems.get(MAVEN.getName());

        mavenAssemblySystem.setPathToRemove(settingsMavenPathToRemoveTextField.getText());
        assemblySystems.put(MAVEN.getName(), mavenAssemblySystem);

        mavenAssemblySystem.setDirToRemove(settingsMavenDirToRemoveTextField.getText());
        assemblySystems.put(MAVEN.getName(), mavenAssemblySystem);

        ConfigManager.getInstance().saveConfig(config);
        settingsSaveButton.setDisable(true);
    }

    @Override
    public void onLanguageChanged(LanguageType languageType) {
        updateLanguage(languageType);
    }
}
