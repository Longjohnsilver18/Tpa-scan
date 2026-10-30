package de.example.tpahunter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanState {
    public record Candidate(String player, String message) {}

    private boolean scanning = false;
    private String itemFilter = "minecraft:diamond";
    private final List<Candidate> candidates = new ArrayList<>();

    public boolean isScanning() { return scanning; }
    public void setScanning(boolean value) { scanning = value; }
    public String getItemFilter() { return itemFilter; }
    public void setItemFilter(String value) { itemFilter = value; }

    public void addCandidate(String player, String message) {
        candidates.add(0, new Candidate(player, message));
        while (candidates.size() > 50) candidates.remove(candidates.size() - 1);
    }

    public List<Candidate> getCandidates() {
        return Collections.unmodifiableList(candidates);
    }

    public void clearCandidates() { candidates.clear(); }
}
