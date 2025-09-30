package ru.mikemimike.builddirsremover.config.manager;

import ru.mikemimike.builddirsremover.util.LanguageType;

public interface LanguageChangeListener {
    void onLanguageChanged(LanguageType languageType);
}
