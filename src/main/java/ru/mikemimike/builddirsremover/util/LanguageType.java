package ru.mikemimike.builddirsremover.util;

import java.util.Arrays;
import java.util.Objects;

public enum LanguageType {
    RUSSIAN("Русский", "ru"),
    ENGLISH("English", "en");

    private String name;
    private String locale;

    LanguageType(String name, String locale) {
        this.name = name;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public String getLocale() {
        return locale;
    }

    public static LanguageType getByName(String name) {
        return Arrays.stream(LanguageType.values())
                .filter(lt -> Objects.equals(name, lt.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown language: " + name));
    }
}
