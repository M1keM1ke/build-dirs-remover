package ru.mikemimike.builddirsremover.config.manager;

import ru.mikemimike.builddirsremover.util.LanguageType;

import java.util.ArrayList;
import java.util.List;

public class LanguageManager {
    private static LanguageManager instance;
    private List<LanguageChangeListener> listeners = new ArrayList<>();

    private LanguageManager() {
        String language = ConfigManager.getInstance().getConfig().getBuildDirsRemover().getSettings().getLanguage();
        setLanguage(LanguageType.getByName(language));
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public void setLanguage(LanguageType languageType) {
        notifyListeners(languageType);
    }

    public void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(LanguageType languageType) {
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged(languageType);
        }
    }
}

