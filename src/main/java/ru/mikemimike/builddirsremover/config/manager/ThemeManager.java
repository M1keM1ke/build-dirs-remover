package ru.mikemimike.builddirsremover.config.manager;

import javafx.scene.Scene;
import ru.mikemimike.builddirsremover.util.ThemeType;


public class ThemeManager {
    private static ThemeManager instance;

    private ThemeManager() {
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void updateTheme(ThemeType theme, Scene scene) {
        switch (theme) {
            case DARK:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/styles/dark-theme.css").toExternalForm());
            break;
            case LIGHT:
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/styles/light-theme.css").toExternalForm());
            break;
            default: throw new RuntimeException("Unknown theme: " + theme);
        }
    }
}
