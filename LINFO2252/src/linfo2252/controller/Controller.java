package linfo2252.controller;

import linfo2252.model.Appointment;
import linfo2252.model.InsuranceLevel;
import linfo2252.model.Model;
import linfo2252.model.SystemState;
import linfo2252.view.MainView;

import java.time.LocalDateTime;
import java.util.Random;

public class Controller implements ControllerInterface {

    private final Model model;
    private MainView view;
    private String lastPage = "Main";
    private String lastAction = "Startup";

    public Controller(Model model) {
        this.model = model;
    }

    public void setMainView(MainView view) {
        this.view = view;
    }

    private SystemState buildCurrentState(String action, String page) {
        return new SystemState(
            LocalDateTime.now().toString(),               // Real Timestamp
            action,                                       // Action
            page,                                         // Page
            model.getFeatureManager().getActiveFeatures(),// Active Features
            model.getCurrentDateTime().toString(),        // Simulated Time
            model.getUserProfile(),                       // User Data
            model.getAppointments(),                      // Future Appts
            model.getAppointmentHistory()                 // Past Appts
        );
    }

    public void logUserAction(String action, String pageName) {
        this.lastAction = action;
        this.lastPage = pageName;
        
        SystemState state = buildCurrentState(action, pageName);
        model.getStateService().saveState(state);
    }

    public boolean isFeatureActive(String name) {
        return model.getFeatureManager().isFeatureActive(name);
    }
    
    public Model getModel() {
        return model;
    }

    // ==========================================
    // 2. FEATURE GATED LOGIC
    // ==========================================

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

        if (view != null) view.showUserView(); // Refresh UI
    }

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
            
            if (isFeatureActive("HistoryTracking")) {
                view.updateHistoryView(model.getAppointmentHistory());
            } else {
                view.updateHistoryView(new java.util.ArrayList<>()); // Clear view if disabled
            }
        }
    }

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
        LocalDateTime randomDate = baseTime.plusDays(rng.nextInt(7) + 1)
                                           .withHour(8 + rng.nextInt(9))
                                           .withMinute(0);

        addAppointment(randomDate, types[index], depts[index]);
    }


    // ==========================================
    // 3. INTERFACE IMPLEMENTATION
    // ==========================================

    @Override
    public int activate(String[] deactivations, String[] activations) {
        var featureManager = model.getFeatureManager();

        if (deactivations != null) {
            for (String name : deactivations) {
                if (featureManager.getFeature(name) == null) return 1;
                featureManager.deactivate(name);
            }
        }

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
        // Reuses the cleaner helper method
        SystemState state = buildCurrentState(lastAction, lastPage);
        return model.getStateService().getStateAsLines(state);
    }
}