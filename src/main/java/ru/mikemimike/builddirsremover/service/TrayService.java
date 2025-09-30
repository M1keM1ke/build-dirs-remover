package ru.mikemimike.builddirsremover.service;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TrayService {
    private static final Logger log = LogManager.getLogger(TrayService.class.getName());
    private Stage primaryStage;
    private TrayIcon trayIcon;

    public TrayService(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUpTray() {
        log.info("Set up tray icon...");
        InputStream resourceAsStream = getClass().getResourceAsStream("/assets/build-dirs-remover-tray-icon.png");

        if (resourceAsStream == null) {
            log.error("Icon resource not found!");
            return;
        }

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource()));

        // Установка подсказки
        trayIcon.setToolTip("Build Dirs Remover");

        PopupMenu popupMenu = new PopupMenu();

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        popupMenu.add(exitItem);
        trayIcon.setPopupMenu(popupMenu);

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Platform.runLater(() -> {
                        primaryStage.show();
                        primaryStage.toFront();
                    });
                }
            }
        });

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
