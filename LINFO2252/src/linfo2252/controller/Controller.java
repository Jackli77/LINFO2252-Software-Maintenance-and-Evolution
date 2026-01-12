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

    public Model getModel() {
        return model;
    }

 // Helper to check feature status quickly
    public boolean isFeatureActive(String name) {
        var f = model.getFeatureManager().getFeature(name);
        return f != null && f.isActive();
    }

    public void addAppointment(LocalDateTime date, String type, String dept) {
        // 1. CHECK FEATURE
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
        // 1. CHECK FEATURE
        if (!isFeatureActive("AppointmentManagement")) {
            System.out.println(">> BLOCKED: AppointmentManagement is disabled.");
            return;
        }

        model.removeAppointment(appt);
        logUserAction("RemoveAppointment", "Appointments");
        if (view != null) view.updateAppointmentView(model.getAppointments());
    }

    public void advanceDays(int days) {
        // 1. CHECK FEATURE
        if (!isFeatureActive("TimeSimulation")) {
            System.out.println(">> BLOCKED: TimeSimulation is disabled.");
            return;
        }

        model.advanceDays(days);
        logUserAction("AdvanceTime_+" + days + "days", "TimeControlPanel");
        
        if (view != null) {
            view.updateDateDisplay(model.getCurrentDateTime());
            view.updateAppointmentView(model.getAppointments());
            
            // Only update history view if History feature is active
            if (isFeatureActive("HistoryTracking")) {
                view.updateHistoryView(model.getAppointmentHistory());
            } else {
                view.updateHistoryView(new java.util.ArrayList<>()); 
            }
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
        addAppointment(randomDate, type, dept);
    }
    
    public void updateUserProfile(String name, String email, String phone, 
	            linfo2252.model.InsuranceLevel insurance) {	
		// 1. Update Model directly
		var profile = model.getUserProfile();
		profile.setName(name);
		profile.setEmail(email);
		profile.setPhoneNumber(phone);
		profile.setInsurance(insurance);
		
		// 2. Log the change (Crucial: Shows the new state in JSON)
		logUserAction("UpdateProfile", "UserProfile");
		
		// 3. Refresh View if active
		if (view != null) {
		// Force a repaint of the user view to show new data/colors
		// (Assumes MainView has a method to refresh the current tab)
		view.showUserView(); 
		}
	}
}