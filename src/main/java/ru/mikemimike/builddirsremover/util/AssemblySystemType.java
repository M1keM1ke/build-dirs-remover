package ru.mikemimike.builddirsremover.util;

public enum AssemblySystemType {
    MAVEN("maven"),
    GRADLE("gradle");

    private String name;

    AssemblySystemType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
