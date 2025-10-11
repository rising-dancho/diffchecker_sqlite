package com.diffchecker.components;

import java.util.ArrayList;
import java.util.List;

public class SyntaxManager {

    private static final List<SyntaxHighlightable> components = new ArrayList<>();
    private static String currentSyntax = "text/plain"; // default

    public static void register(SyntaxHighlightable comp) {
        components.add(comp);
        comp.applySyntaxStyle(currentSyntax);
    }

    public static void setSyntax(String syntaxStyle) {
        currentSyntax = syntaxStyle;
        for (SyntaxHighlightable comp : components) {
            comp.applySyntaxStyle(syntaxStyle);
        }
    }

    public static String getCurrentSyntax() {
        return currentSyntax;
    }
}
