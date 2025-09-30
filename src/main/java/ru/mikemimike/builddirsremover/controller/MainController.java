package ru.mikemimike.builddirsremover.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import ru.mikemimike.builddirsremover.config.manager.ConfigManager;
import ru.mikemimike.builddirsremover.config.manager.LanguageChangeListener;
import ru.mikemimike.builddirsremover.config.manager.LanguageManager;
import ru.mikemimike.builddirsremover.util.LanguageType;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements LanguageChangeListener {
    @FXML
    private Menu mainMenu;

    @FXML
    private Menu mainMenuAssemblySystem;

    @FXML
    private MenuItem mainMenuSettingsItem;

    @FXML
    private BorderPane mainLayout;


    @FXML
    public void initialize() {
        LanguageManager.getInstance().addLanguageChangeListener(this);
        String language = ConfigManager.getInstance().getConfig().getBuildDirsRemover().getSettings().getLanguage();
        LanguageManager.getInstance().setLanguage(LanguageType.getByName(language));
    }

    public void updateLanguage(LanguageType languageType) {
        Locale locale = Locale.of(languageType.getLocale());
        ResourceBundle bundle = ResourceBundle.getBundle("languages.language", locale);

        mainMenu.setText(bundle.getString("menu"));
        mainMenuAssemblySystem.setText(bundle.getString("menuAssemblySystem"));
        mainMenuSettingsItem.setText(bundle.getString("menuSettings"));
    }

    @FXML
    public void handleGradleSelection() {
        try {
            VBox vBox = FXMLLoader.load(getClass().getResource("/view/gradle-assembly-system-menu-item.fxml"));
            mainLayout.setCenter(vBox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleMavenSelection() {
        try {
            VBox vBox = FXMLLoader.load(getClass().getResource("/view/maven-assembly-system-menu-item.fxml"));
            mainLayout.setCenter(vBox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showSettings() {
        mainLayout.setCenter(null);

        try {
            ScrollPane settingsScrollPane = FXMLLoader.load(getClass().getResource("/view/settings.fxml"));
            mainLayout.setCenter(settingsScrollPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLanguageChanged(LanguageType languageType) {
        updateLanguage(languageType);
    }
}
