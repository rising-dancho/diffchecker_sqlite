package com.diffchecker.components;

import java.util.*;
import java.util.prefs.Preferences;

public class ThemeManager {
    private static final String PREF_NODE = "com.diffchecker";
    private static final String PREF_KEY = "darkThemeEnabled";
    private static boolean darkThemeEnabled;

    private static final List<ThemedComponent> components = new ArrayList<>();

    static {
        Preferences prefs = Preferences.userRoot().node(PREF_NODE);
        darkThemeEnabled = prefs.getBoolean(PREF_KEY, true); // default dark
    }

    public static boolean isDarkTheme() { return darkThemeEnabled; }

    public static void toggleTheme() { setDarkTheme(!darkThemeEnabled); }

    public static void setDarkTheme(boolean dark) {
        darkThemeEnabled = dark;
        Preferences.userRoot().node(PREF_NODE).putBoolean(PREF_KEY, dark);
        applyThemeToAll();
    }

    public static void register(ThemedComponent comp) {
        components.add(comp);
        comp.applyTheme(darkThemeEnabled);
    }

    public static void applyThemeToAll() {
        for (ThemedComponent comp : components) {
            comp.applyTheme(darkThemeEnabled);
        }
    }
}
