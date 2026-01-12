package linfo2252.model;

/**
 * Represents a discrete, toggleable feature within the application.
 * <p>
 * This class acts as a "Feature Flag" wrapper. It maintains the activation state (ON/OFF)
 * of a specific system capability (e.g., "AppointmentManagement" or "TimeSimulation").
 * The {@link linfo2252.controller.Controller} checks the state of these objects before
 * executing business logic.
 */
public class Feature { 
    
    private final String name;
    private boolean active;
    
    /**
     * Constructs a new Feature with the specified unique name.
     * Features are initialized to the 'inactive' (OFF) state by default.
     * * @param name The unique identifier for this feature (e.g., "HistoryTracking").
     */
    public Feature(String name) {
        this.name = name;
        this.active = false;
    }
    
    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getName() {
        return name;
    }
    
    /**
     * Checks if the feature is currently enabled.
     * * @return {@code true} if the feature is active and usable; {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }
    
    // -------------------------------------------------------------------------
    // State Mutators
    // -------------------------------------------------------------------------

    /**
     * Enables the feature.
     * If the feature is already active, this operation has no effect.
     */
    public void activate() {
        if (!active) {
            active = true;
        }
    }
    
    /**
     * Disables the feature.
     * If the feature is already inactive, this operation has no effect.
     */
    public void deactivate() {
        if (active) {
            active = false;
        }
    }
}