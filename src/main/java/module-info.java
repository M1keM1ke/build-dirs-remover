module ru.mikemimike.builddirsremover {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.yaml.snakeyaml;
    requires java.desktop;
    requires org.apache.logging.log4j;

    opens ru.mikemimike.builddirsremover to javafx.fxml;
    opens ru.mikemimike.builddirsremover.controller to javafx.fxml;
    exports ru.mikemimike.builddirsremover;
    exports ru.mikemimike.builddirsremover.properties;
}