package linfo2252.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import linfo2252.model.features.FeatureManager;
import linfo2252.model.features.UserFeature;
import linfo2252.model.features.AppointmentFeature;
// 1. Import your StateService (if it's in a different package)
// import linfo2252.model.StateService; 

public class Model {

    private LocalDateTime currentDateTime;
    private PriorityQueue<ScheduledEvent> eventQueue;
    private List<Appointment> appointments;
    private List<Appointment> appointmentHistory;
    private FeatureManager featureManager;
    private UserProfile userProfile;
    
    // 2. Add the field for StateService
    private StateService stateService; 

    public Model() {
        this.currentDateTime = LocalDateTime.now();
        this.eventQueue = new PriorityQueue<>();
        this.appointments = new ArrayList<>();
        this.appointmentHistory = new ArrayList<>();
        this.userProfile = new UserProfile();

        this.featureManager = new FeatureManager();
        this.featureManager.registerFeature(new UserFeature());
        this.featureManager.registerFeature(new AppointmentFeature());

        // 3. Initialize the service
        this.stateService = new StateService();
    }

    // 4. Add the getter so the Controller can access it
    public StateService getStateService() {
        return stateService;
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public List<Appointment> getAppointmentHistory() {
        return new ArrayList<>(appointmentHistory);
    }

    // ===== TIME EVENT SIMULATION ===== //

    public void advanceDays(int days) {
        currentDateTime = currentDateTime.plusDays(days);
        runDueEvents();
        movePastAppointmentsToHistory();
    }

    private void movePastAppointmentsToHistory() {
        List<Appointment> toMove = new ArrayList<>();

        for (Appointment a : appointments) {
            if (a.getDateTime().isBefore(currentDateTime)) {
                toMove.add(a);
            }
        }

        appointments.removeAll(toMove);
        appointmentHistory.addAll(toMove);
    }

    private void runDueEvents() {
        while (!eventQueue.isEmpty() && !eventQueue.peek().dateTime().isBefore(currentDateTime)) {
            ScheduledEvent ev = eventQueue.poll();
            ev.action().run();
        }
    }

    public void scheduleEvent(LocalDateTime dateTime, Runnable action) {
        eventQueue.add(new ScheduledEvent(dateTime, action));
    }

    // ===== APPOINTMENTS ===== //

    public boolean createAppointment(LocalDateTime dateTime, String type, String department) {
        if (dateTime.isBefore(currentDateTime)) return false;

        Appointment appt = new Appointment(dateTime, type, department);
        appointments.add(appt);

        scheduleEvent(dateTime.minusDays(1), () ->
                System.out.println("Reminder: \"" + type + "\" at " + department + " happens tomorrow."));

        scheduleEvent(dateTime, () ->
                System.out.println("Appointment NOW: " + type + " (" + department + ")"));

        return true;
    }

    public void removeAppointment(Appointment appt) {
        appointments.remove(appt);
    }

    private record ScheduledEvent(LocalDateTime dateTime, Runnable action)
            implements Comparable<ScheduledEvent> {

        @Override
        public int compareTo(ScheduledEvent o) {
            return this.dateTime.compareTo(o.dateTime);
        }
    }
    
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void loadSampleData() {
        appointments.add(new Appointment(currentDateTime.plusDays(2), "Consultation", "Cardiology"));
        appointments.add(new Appointment(currentDateTime.plusDays(5), "Dental Cleaning", "Dentistry"));
        appointments.add(new Appointment(currentDateTime.plusDays(8), "Eye Checkup", "Ophthalmology"));

        appointmentHistory.add(new Appointment(currentDateTime.minusDays(3), "Vaccination", "General Medicine"));
        appointmentHistory.add(new Appointment(currentDateTime.minusDays(15), "Blood Work", "Laboratory"));
        
        userProfile.setName("John Doe");
        userProfile.setEmail("john.doe@example.com");
        userProfile.setPhoneNumber("+123456789");
        userProfile.setInsurance(InsuranceLevel.STANDARD);
        userProfile.setAccountType(AccountType.PRIMARY_USER);
    }
}