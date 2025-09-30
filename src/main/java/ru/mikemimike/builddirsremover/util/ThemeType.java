package ru.mikemimike.builddirsremover.util;

import java.util.Arrays;
import java.util.Objects;

public enum ThemeType {
    DARK("Dark"),
    LIGHT("Light");

    private String name;

    ThemeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ThemeType getByName(String name) {
        return Arrays.stream(ThemeType.values())
                .filter(tu -> Objects.equals(name, tu.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown theme: " + name));
    }
}
