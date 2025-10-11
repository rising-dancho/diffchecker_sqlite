package com.diffchecker.components;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class SyntaxManager {

    private static final String PREF_NODE = "com.diffchecker.syntax";
    private static final String PREF_KEY  = "currentSyntaxStyle";

    private static final List<SyntaxHighlightable> components = new ArrayList<>();

    // Load last-used syntax (default to "text/plain")
    private static String currentSyntax;

    static {
        Preferences prefs = Preferences.userRoot().node(PREF_NODE); // SAVE THE SYNTAX IN THE OS PREFERENCES
        currentSyntax = prefs.get(PREF_KEY, "text/plain");
    }

    public static void register(SyntaxHighlightable comp) {
        components.add(comp);
        comp.applySyntaxStyle(currentSyntax);
    }

    public static void setSyntax(String syntaxStyle) {
        currentSyntax = syntaxStyle;

        // Persist the new style
        Preferences.userRoot()
            .node(PREF_NODE)
            .put(PREF_KEY, syntaxStyle);

        // Apply to all registered components
        for (SyntaxHighlightable comp : components) {
            comp.applySyntaxStyle(syntaxStyle);
        }
    }

    public static String getCurrentSyntax() {
        return currentSyntax;
    }
}
