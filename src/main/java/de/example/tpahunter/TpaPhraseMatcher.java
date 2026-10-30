package de.example.tpahunter;

import java.text.Normalizer;
import java.util.Locale;

public final class TpaPhraseMatcher {
    private TpaPhraseMatcher() {}

    public static boolean looksLikeTpaOffer(String input) {
        if (input == null || input.isBlank()) return false;

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);

        boolean mentionsTpa = normalized.matches("(?s).*\\btpa\\b.*")
                || normalized.matches("(?s).*\\btp\\s*a\\b.*");
        if (!mentionsTpa) return false;

        return normalized.contains("fur ")
                || normalized.contains("wer will")
                || normalized.contains("wer mochte")
                || normalized.contains("wer möchte")
                || normalized.contains("wer braucht")
                || normalized.contains("tp zu mir")
                || normalized.contains("teleport")
                || normalized.contains("tpa?")
                || normalized.contains("tpa pls")
                || normalized.contains("tpa bitte");
    }
}
