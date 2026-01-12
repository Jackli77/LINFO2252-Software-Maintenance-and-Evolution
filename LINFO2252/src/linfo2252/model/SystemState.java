package linfo2252.model;

import java.util.List;

public class SystemState {
    private final String timestamp;
    private final String lastAction;
    private final String currentPage;
    private final List<String> activeFeatures;
    private final String simulatedDate;
    private final UserProfile userProfile;
    private final List<Appointment> appointments;
    private final List<Appointment> history;

    public SystemState(String timestamp, String lastAction, String currentPage, 
                       List<String> activeFeatures, String simulatedDate,
                       UserProfile user, List<Appointment> appts, List<Appointment> history) {
        this.timestamp = timestamp;
        this.lastAction = lastAction;
        this.currentPage = currentPage;
        this.activeFeatures = activeFeatures;
        this.simulatedDate = simulatedDate;
        this.userProfile = user;
        this.appointments = appts;
        this.history = history;
    }

    // Getters
    public String getTimestamp() { return timestamp; }
    public String getLastAction() { return lastAction; }
    public String getCurrentPage() { return currentPage; }
    public List<String> getActiveFeatures() { return activeFeatures; }
    public String getSimulatedDate() { return simulatedDate; }
    public UserProfile getUserProfile() { return userProfile; }
    public List<Appointment> getAppointments() { return appointments; }
    public List<Appointment> getHistory() { return history; }
}