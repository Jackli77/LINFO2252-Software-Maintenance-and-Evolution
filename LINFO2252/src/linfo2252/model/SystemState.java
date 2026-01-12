package linfo2252.model;

import java.util.List;

/**
 * Represents an immutable snapshot of the entire application state.
 * <p>
 * This Data Transfer Object (DTO) captures the system's status at a specific moment in time.
 * It is primarily used by the {@link StateService} for logging, persistence (JSON export),
 * and debugging purposes.
 */
public class SystemState {

    // Metadata
    private final String timestamp;
    private final String lastAction;
    private final String currentPage;
    private final String simulatedDate;

    // Core Data
    private final List<String> activeFeatures;
    private final UserProfile userProfile;
    private final List<Appointment> appointments;
    private final List<Appointment> history;

    /**
     * Constructs a complete state snapshot.
     * All collections passed here should ideally be defensive copies to ensure immutability.
     *
     * @param timestamp      The real-world time of the snapshot.
     * @param lastAction     The user action that triggered this state change.
     * @param currentPage    The view/page the user was on.
     * @param activeFeatures List of currently enabled feature flags.
     * @param simulatedDate  The virtual date within the simulation logic.
     * @param user           The current user profile data.
     * @param appts          List of future scheduled appointments.
     * @param history        List of past/archived appointments.
     */
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

    // -------------------------------------------------------------------------
    // Accessors (Getters)
    // -------------------------------------------------------------------------

    public String getTimestamp() { return timestamp; }
    
    public String getLastAction() { return lastAction; }
    
    public String getCurrentPage() { return currentPage; }
    
    public List<String> getActiveFeatures() { return activeFeatures; }
    
    public String getSimulatedDate() { return simulatedDate; }
    
    public UserProfile getUserProfile() { return userProfile; }
    
    public List<Appointment> getAppointments() { return appointments; }
    
    public List<Appointment> getHistory() { return history; }
}