package ru.mikemimike.builddirsremover.properties;

import java.util.Map;

public class BuildDirsRemoverProperties {
    private BuildDirsRemover buildDirsRemover;

    public BuildDirsRemoverProperties() {
    }

    public BuildDirsRemover getBuildDirsRemover() {
        return buildDirsRemover;
    }

    public void setBuildDirsRemover(BuildDirsRemover buildDirsRemover) {
        this.buildDirsRemover = buildDirsRemover;
    }

    public static class BuildDirsRemover {
        private Settings settings;

        public BuildDirsRemover() {
        }

        public Settings getSettings() {
            return settings;
        }

        public void setSettings(Settings settings) {
            this.settings = settings;
        }

        public static class Settings {
            private String pathToRemove;
            private String language;
            private String theme;
            private Map<String, AssemblySystem> assemblySystems;

            public Settings() {
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getTheme() {
                return theme;
            }

            public void setTheme(String theme) {
                this.theme = theme;
            }

            public Map<String, AssemblySystem> getAssemblySystems() {
                return assemblySystems;
            }

            public void setAssemblySystems(Map<String, AssemblySystem> assemblySystems) {
                this.assemblySystems = assemblySystems;
            }

            public static class AssemblySystem {
                private String pathToRemove;
                private String dirToRemove;

                public AssemblySystem() {
                }

                public String getPathToRemove() {
                    return pathToRemove;
                }

                public void setPathToRemove(String pathToRemove) {
                    this.pathToRemove = pathToRemove;
                }

                public String getDirToRemove() {
                    return dirToRemove;
                }

                public void setDirToRemove(String dirToRemove) {
                    this.dirToRemove = dirToRemove;
                }
            }
        }
    }
}
