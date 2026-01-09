package linfo2252.controller;

import linfo2252.model.Appointment;
import linfo2252.model.Model;
import linfo2252.model.SystemState;
import linfo2252.view.MainView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class Controller implements ControllerInterface {

    private Model model;
    private MainView view; // Can be null if disabled

    // Track the last known page/action internally for logs
    private String lastPage = "Unknown";
    private String lastAction = "SystemStart";

    public Controller(Model model) {
        this.model = model;
    }

    // Helper to log actions internally + update JSON file
    public void logUserAction(String action, String pageName) {
        this.lastAction = action;
        this.lastPage = pageName;
        
        // Gather Comprehensive Data from Model
        SystemState state = new SystemState(
            LocalDateTime.now().toString(),               // Real Timestamp
            action,                                       // Action
            pageName,                                     // Page
            model.getFeatureManager().getActiveFeatures(),// Features
            
            // --- NEW DATA ---
            model.getCurrentDateTime().toString(),        // Simulated Time
            model.getUserProfile(),                       // User Data
            model.getAppointments(),                      // Future Appts
            model.getAppointmentHistory()                 // Past Appts
        );

        model.getStateService().saveState(state);
    }

    // ==========================================
    // INTERFACE IMPLEMENTATION
    // ==========================================

    @Override
    public int activate(String[] deactivations, String[] activations) {
        var featureManager = model.getFeatureManager();

        // Handle Deactivations
        if (deactivations != null) {
            for (String name : deactivations) {
                // Check if feature exists (simple validation)
                if (featureManager.getFeature(name) == null) return 1; // Error code 1: Feature not found
                featureManager.deactivate(name);
            }
        }

        // Handle Activations
        if (activations != null) {
            for (String name : activations) {
                if (featureManager.getFeature(name) == null) return 1; 
                featureManager.activate(name);
            }
        }

        // Log this system change
        logUserAction("FeaturesUpdated", lastPage);
        
        // If View is active, we might need to refresh it
        if (view != null) {
            view.repaint(); // or a more specific refresh method
        }

        return 0; // Success
    }

    @Override
    public boolean enableUIView() {
        if (this.view != null && this.view.isVisible()) {
            return false; // Already enabled/visible
        }

        try {
            // Create and show the view
            // Note: MainView constructor calls setVisible(true)
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
        if (this.view == null) {
            return true; // Already disabled, strictly speaking this is a "success" state
        }

        this.view.dispose(); // Close the JFrame
        this.view = null;
        
        logUserAction("ViewDisabled", lastPage);
        return true;
    }

    @Override
    public String[] getStateAsLog() {
        SystemState state = new SystemState(
            LocalDateTime.now().toString(),
            lastAction,
            lastPage,
            model.getFeatureManager().getActiveFeatures(),
            model.getCurrentDateTime().toString(),
            model.getUserProfile(),
            model.getAppointments(),
            model.getAppointmentHistory()
        );
        return model.getStateService().getStateAsLines(state);
    }

    // Getter for View to use
    public Model getModel() {
        return model;
    }
    
 // In linfo2252.controller.Controller

    public void addAppointment(LocalDateTime date, String type, String dept) {
        // 1. Validate and Update Model
        if (date == null || type.isEmpty() || dept.isEmpty()) return;

        boolean success = model.createAppointment(date, type, dept);

        if (success) {
            // 2. Log the action (Updates JSON)
            logUserAction("AddAppointment", "Appointments");

            // 3. Refresh the View
            if (view != null) {
                view.updateAppointmentView(model.getAppointments());
            }
        } else {
            // Optional: Handle failure (e.g. past date)
            System.out.println("Could not create appointment (Date in past?)");
        }
    }

    public void removeAppointment(Appointment appt) {
        // 1. Update Model
        model.removeAppointment(appt);

        // 2. Log the action
        logUserAction("RemoveAppointment", "Appointments");

        // 3. Refresh the View
        if (view != null) {
            view.updateAppointmentView(model.getAppointments());
        }
    }
    
    public void advanceDays(int days) {
        // 1. Update the Model
        model.advanceDays(days);

        // 2. Log the Action (StateService)
        logUserAction("AdvanceTime_+" + days + "days", "TimeControlPanel");

        // 3. Update the View
        // We must check if view is null (in case UI is disabled via disableUIView)
        if (view != null) {
            // Update the text label
            view.updateDateDisplay(model.getCurrentDateTime());
            
            // IMPORTANT: Advancing time might move appointments to history, 
            // so we must refresh those lists too!
            view.updateAppointmentView(model.getAppointments());
            view.updateHistoryView(model.getAppointmentHistory());
        }
    }
    
    public void createRandomAppointment() {
        Random rng = new Random();

        // 1. Data Definitions
        String[] types = { 
            "General Checkup", "Blood Test", "X-Ray", "MRI Scan", 
            "Vaccination", "Dental Cleaning", "Eye Exam", "Physiotherapy" 
        };
        String[] depts = { 
            "General Medicine", "Laboratory", "Radiology", "Radiology", 
            "Pediatrics", "Dentistry", "Ophthalmology", "Rehabilitation" 
        };

        // 2. Pick Random Data
        int index = rng.nextInt(types.length);
        String type = types[index];
        String dept = depts[index];

        // 3. Pick Random Time (1-7 days in future, 08:00-17:00)
        LocalDateTime baseTime = model.getCurrentDateTime();
        int daysFuture = rng.nextInt(7) + 1;
        int hour = 8 + rng.nextInt(10);
        int minute = rng.nextBoolean() ? 0 : 30;

        LocalDateTime randomDate = baseTime.plusDays(daysFuture)
                                           .withHour(hour)
                                           .withMinute(minute);

        // 4. Reuse your existing method!
        // This ensures it gets logged to JSON and the View refreshes automatically.
        addAppointment(randomDate, type, dept);
    }
}