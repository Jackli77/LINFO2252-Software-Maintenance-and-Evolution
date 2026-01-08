package linfo2252.model;

import java.util.List;

public class SystemState {
    private String timestamp;
    private String lastAction;
    private String currentPage;
    private List<String> activeFeatures;

    public SystemState(String timestamp, String lastAction, String currentPage, List<String> activeFeatures) {
        this.timestamp = timestamp;
        this.lastAction = lastAction;
        this.currentPage = currentPage;
        this.activeFeatures = activeFeatures;
    }

    // Getters
    public String getTimestamp() { return timestamp; }
    public String getLastAction() { return lastAction; }
    public String getCurrentPage() { return currentPage; }
    public List<String> getActiveFeatures() { return activeFeatures; }
}