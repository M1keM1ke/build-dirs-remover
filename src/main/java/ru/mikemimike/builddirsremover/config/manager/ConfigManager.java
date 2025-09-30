package ru.mikemimike.builddirsremover.config.manager;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;
import ru.mikemimike.builddirsremover.properties.BuildDirsRemoverProperties;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static ru.mikemimike.builddirsremover.builder.BuildDirsRemoverBuilder.buildDefaultBuildDirsRemoverProperties;

public class ConfigManager {
    private static ConfigManager instance;
    private BuildDirsRemoverProperties config;
    private final String baseConfigFilePath = "config.yml";

    private ConfigManager() {
        loadConfig();
    }

    public BuildDirsRemoverProperties getConfig() {
        return config;
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public void loadConfig() {
        var loaderoptions = new LoaderOptions();
        TagInspector taginspector =
                tag -> tag.getClassName().equals(BuildDirsRemoverProperties.class.getName());
        loaderoptions.setTagInspector(taginspector);
        Yaml yaml = new Yaml(new Constructor(BuildDirsRemoverProperties.class, loaderoptions));

        try {
            // Проверяем, существует ли базовый файл конфигурации
            File baseConfigFile = new File(baseConfigFilePath);
            if (!baseConfigFile.exists()) {
                // Если базовый файл не существует, создаем новый объект конфигурации с настройками по умолчанию
                config = buildDefaultBuildDirsRemoverProperties();
                saveConfig(config);
            } else {
                // Загружаем конфигурацию из базового файла
                try (InputStream inputStreamBaseConfig = new FileInputStream(baseConfigFile)) {
                    config = yaml.loadAs(inputStreamBaseConfig, BuildDirsRemoverProperties.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void saveConfig(BuildDirsRemoverProperties config) {
        Yaml yaml = new Yaml();
        try (OutputStream outputStream = new FileOutputStream(baseConfigFilePath);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            yaml.dump(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
