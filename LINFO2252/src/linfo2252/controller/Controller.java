package linfo2252.controller;

import linfo2252.model.Appointment;
import linfo2252.model.InsuranceLevel;
import linfo2252.model.Model;
import linfo2252.model.SystemState;
import linfo2252.view.MainView;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * The main Controller for the application.
 * <p>
 * This class acts as the mediator between the {@link Model} and the {@link MainView}.
 * It handles user actions, enforces feature toggles, and manages the system's logging state.
 */
public class Controller implements ControllerInterface {

    private final Model model;
    private MainView view;
    
    // Tracking context for logging purposes
    private String lastPage = "Main";
    private String lastAction = "Startup";

    public Controller(Model model) {
        this.model = model;
    }

    public void setMainView(MainView view) {
        this.view = view;
    }

    /**
     * Constructs a snapshot of the current system state.
     * Used for logging and persistence.
     */
    private SystemState buildCurrentState(String action, String page) {
        return new SystemState(
            LocalDateTime.now().toString(),
            action,
            page,
            model.getFeatureManager().getActiveFeatures(),
            model.getCurrentDateTime().toString(),
            model.getUserProfile(),
            model.getAppointments(),
            model.getAppointmentHistory()
        );
    }

    /**
     * Updates the internal tracking state and persists the full system state to the service.
     *
     * @param action   The specific action performed (e.g., "AddAppointment").
     * @param pageName The context/page where the action occurred.
     */
    public void logUserAction(String action, String pageName) {
        this.lastAction = action;
        this.lastPage = pageName;
        
        SystemState state = buildCurrentState(action, pageName);
        model.getStateService().saveState(state);
    }

    /**
     * Checks if a specific feature is currently enabled in the FeatureManager.
     */
    public boolean isFeatureActive(String name) {
        return model.getFeatureManager().isFeatureActive(name);
    }
    
    public Model getModel() {
        return model;
    }

    // -------------------------------------------------------------------------
    // Business Logic & Feature Gating
    // -------------------------------------------------------------------------

    /**
     * Updates user profile data if the 'UserProfile' feature is active.
     */
    public void updateUserProfile(String name, String email, String phone, InsuranceLevel insurance) {
        if (!isFeatureActive("UserProfile")) {
            System.out.println(">> BLOCKED: UserProfile feature is disabled.");
            return;
        }

        var profile = model.getUserProfile();
        profile.setName(name);
        profile.setEmail(email);
        profile.setPhoneNumber(phone);
        profile.setInsurance(insurance);

        logUserAction("UpdateProfile", "UserProfile");

        if (view != null) view.showUserView();
    }

    /**
     * Creates a new appointment if the 'AppointmentManagement' feature is active.
     */
    public void addAppointment(LocalDateTime date, String type, String dept) {
        if (!isFeatureActive("AppointmentManagement")) {
            System.out.println(">> BLOCKED: AppointmentManagement is disabled.");
            return;
        }

        if (date == null || type.isEmpty()) return;

        boolean success = model.createAppointment(date, type, dept);
        if (success) {
            logUserAction("AddAppointment", "Appointments");
            if (view != null) view.updateAppointmentView(model.getAppointments());
        }
    }

    public void removeAppointment(Appointment appt) {
        if (!isFeatureActive("AppointmentManagement")) {
            System.out.println(">> BLOCKED: AppointmentManagement is disabled.");
            return;
        }

        model.removeAppointment(appt);
        logUserAction("RemoveAppointment", "Appointments");
        if (view != null) view.updateAppointmentView(model.getAppointments());
    }

    /**
     * Advances the simulated system time.
     * Also refreshes the view and moves past appointments to history.
     */
    public void advanceDays(int days) {
        if (!isFeatureActive("TimeSimulation")) {
            System.out.println(">> BLOCKED: TimeSimulation is disabled.");
            return;
        }

        model.advanceDays(days);
        logUserAction("AdvanceTime_+" + days + "days", "TimeControlPanel");

        if (view != null) {
            view.updateDateDisplay(model.getCurrentDateTime());
            view.updateAppointmentView(model.getAppointments());
            
            // Graceful degradation: clear history view if the feature is disabled
            if (isFeatureActive("HistoryTracking")) {
                view.updateHistoryView(model.getAppointmentHistory());
            } else {
                view.updateHistoryView(new java.util.ArrayList<>());
            }
        }
    }

    /**
     * Generates a random appointment for testing/demo purposes.
     */
    public void createRandomAppointment() {
        if (!isFeatureActive("AppointmentManagement")) {
            System.out.println(">> BLOCKED: AppointmentManagement is disabled.");
            return;
        }

        Random rng = new Random();
        String[] types = { "General Checkup", "Blood Test", "X-Ray", "MRI Scan", "Vaccination" };
        String[] depts = { "General Medicine", "Laboratory", "Radiology", "Radiology", "Pediatrics" };

        int index = rng.nextInt(types.length);
        LocalDateTime baseTime = model.getCurrentDateTime();
        
        // Schedule 1-7 days in the future, between 08:00 and 16:00
        LocalDateTime randomDate = baseTime.plusDays(rng.nextInt(7) + 1)
                                           .withHour(8 + rng.nextInt(9))
                                           .withMinute(0);

        addAppointment(randomDate, types[index], depts[index]);
    }

    // -------------------------------------------------------------------------
    // ControllerInterface Implementation
    // -------------------------------------------------------------------------

    @Override
    public int activate(String[] deactivations, String[] activations) {
        var featureManager = model.getFeatureManager();

        // Handle deactivations
        if (deactivations != null) {
            for (String name : deactivations) {
                if (featureManager.getFeature(name) == null) return 1;
                featureManager.deactivate(name);
            }
        }

        // Handle activations
        if (activations != null) {
            for (String name : activations) {
                if (featureManager.getFeature(name) == null) return 1;
                featureManager.activate(name);
            }
        }

        logUserAction("FeaturesUpdated", lastPage);
        return 0;
    }

    @Override
    public boolean enableUIView() {
        if (this.view != null && this.view.isVisible()) return false;
        try {
            this.view = new MainView(this);
            logUserAction("ViewEnabled", lastPage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean disableUIView() {
        if (this.view == null) return true;
        this.view.dispose();
        this.view = null;
        logUserAction("ViewDisabled", lastPage);
        return true;
    }

    @Override
    public String[] getStateAsLog() {
        SystemState state = buildCurrentState(lastAction, lastPage);
        return model.getStateService().getStateAsLines(state);
    }
}