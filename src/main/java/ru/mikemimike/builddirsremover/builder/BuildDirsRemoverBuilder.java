package ru.mikemimike.builddirsremover.builder;

import ru.mikemimike.builddirsremover.properties.BuildDirsRemoverProperties;
import ru.mikemimike.builddirsremover.util.AssemblySystemType;
import ru.mikemimike.builddirsremover.util.LanguageType;
import ru.mikemimike.builddirsremover.util.ThemeType;

import java.util.HashMap;
import java.util.Map;

public class BuildDirsRemoverBuilder {

    public static BuildDirsRemoverProperties buildDefaultBuildDirsRemoverProperties() {
        BuildDirsRemoverProperties buildDirsRemoverProperties = new BuildDirsRemoverProperties();
        BuildDirsRemoverProperties.BuildDirsRemover buildDirsRemover = new BuildDirsRemoverProperties.BuildDirsRemover();
        BuildDirsRemoverProperties.BuildDirsRemover.Settings settings = new BuildDirsRemoverProperties.BuildDirsRemover.Settings();
        settings.setLanguage(LanguageType.ENGLISH.getName());
        settings.setTheme(ThemeType.DARK.getName());

        BuildDirsRemoverProperties.BuildDirsRemover.Settings.AssemblySystem maven =
                new BuildDirsRemoverProperties.BuildDirsRemover.Settings.AssemblySystem();
        maven.setDirToRemove("target");
        BuildDirsRemoverProperties.BuildDirsRemover.Settings.AssemblySystem gradle =
                new BuildDirsRemoverProperties.BuildDirsRemover.Settings.AssemblySystem();
        maven.setDirToRemove("build");

        settings.setAssemblySystems(
                new HashMap<>(
                        Map.of(
                                AssemblySystemType.MAVEN.getName(), maven,
                                AssemblySystemType.GRADLE.getName(), gradle
                        )
                )
        );

        buildDirsRemover.setSettings(settings);
        buildDirsRemoverProperties.setBuildDirsRemover(buildDirsRemover);
        return buildDirsRemoverProperties;
    }
}
