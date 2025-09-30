package ru.mikemimike.builddirsremover;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.mikemimike.builddirsremover.config.manager.ConfigManager;
import ru.mikemimike.builddirsremover.config.manager.ThemeManager;
import ru.mikemimike.builddirsremover.service.TrayService;
import ru.mikemimike.builddirsremover.util.ThemeType;

public class BuildDirsRemoverApplication extends Application {
    private TrayService trayService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //allows the application to run in the background
        Platform.setImplicitExit(false);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_view.fxml"));
            Scene scene = new Scene(loader.load(), 900, 400);
            primaryStage.setTitle("Build Dirs Remover");

            ThemeManager.getInstance().updateTheme(
                    ThemeType.getByName(ConfigManager.getInstance().getConfig().getBuildDirsRemover().getSettings().getTheme()),
                    scene
            );

            primaryStage.setOnCloseRequest(event -> {
                event.consume();
                primaryStage.hide();
            });

            primaryStage.setScene(scene);
            primaryStage.show();

            trayService = new TrayService(primaryStage);
            trayService.setUpTray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
