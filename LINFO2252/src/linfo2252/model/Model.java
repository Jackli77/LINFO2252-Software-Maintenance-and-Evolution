package linfo2252.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The core domain logic and state container for the application.
 * <p>
 * This class encapsulates the entire state of the system, including the current simulated time,
 * active appointments, historical records, and user profile data. It also manages the 
 * event simulation queue for future triggers.
 */
public class Model {

    // --- State Variables ---
    private LocalDateTime currentDateTime;
    private PriorityQueue<ScheduledEvent> eventQueue;
    
    // Core Data Structures
    private List<Appointment> appointments;
    private List<Appointment> appointmentHistory;
    
    // Sub-Systems
    private FeatureManager featureManager;
    private UserProfile userProfile;
    private StateService stateService; 

    /**
     * Initializes the model with default settings, empty lists, and active features.
     */
    public Model() {
        this.currentDateTime = LocalDateTime.now();
        this.eventQueue = new PriorityQueue<>();
        this.appointments = new ArrayList<>();
        this.appointmentHistory = new ArrayList<>();
        this.userProfile = new UserProfile();
        
        // Initialize Feature System
        this.featureManager = new FeatureManager();
        initializeFeatures(); // Extracted for clarity

        // Initialize Persistence Service
        this.stateService = new StateService();
    }
    
    private void initializeFeatures() {
        featureManager.registerFeature(new Feature("AppointmentManagement"));
        featureManager.registerFeature(new Feature("HistoryTracking"));
        featureManager.registerFeature(new Feature("TimeSimulation"));
        featureManager.registerFeature(new Feature("UserProfile"));
        
        // Default: All features active on startup
        featureManager.activate("AppointmentManagement");
        featureManager.activate("HistoryTracking");
        featureManager.activate("TimeSimulation");
        featureManager.activate("UserProfile");
    }

    // ==========================================
    // 1. DATA ACCESSORS (GETTERS)
    // ==========================================

    public StateService getStateService() {
        return stateService;
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    /**
     * Returns a copy of the active appointments list.
     * Defensive copy prevents external modification of the internal list.
     */
    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Returns a copy of the historical appointments list.
     */
    public List<Appointment> getAppointmentHistory() {
        return new ArrayList<>(appointmentHistory);
    }
    
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    // ==========================================
    // 2. TIME SIMULATION LOGIC
    // ==========================================

    /**
     * Advances the simulated clock by a set number of days.
     * Triggers any events scheduled during this period and archives past appointments.
     *
     * @param days The number of days to jump forward.
     */
    public void advanceDays(int days) {
        currentDateTime = currentDateTime.plusDays(days);
        runDueEvents();
        movePastAppointmentsToHistory();
    }

    /**
     * Checks all active appointments against the current time.
     * If an appointment date has passed, it is moved to the history list.
     */
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

    /**
     * Executes any scheduled actions (reminders, notifications) whose trigger time has passed.
     */
    private void runDueEvents() {
        // While there are events, and the earliest event is in the past/present...
        while (!eventQueue.isEmpty() && !eventQueue.peek().dateTime().isBefore(currentDateTime)) {
            ScheduledEvent ev = eventQueue.poll();
            ev.action().run();
        }
    }

    public void scheduleEvent(LocalDateTime dateTime, Runnable action) {
        eventQueue.add(new ScheduledEvent(dateTime, action));
    }

    // ==========================================
    // 3. APPOINTMENT LOGIC
    // ==========================================

    /**
     * Creates a new appointment and schedules associated reminders.
     *
     * @return true if created successfully; false if the date is in the past.
     */
    public boolean createAppointment(LocalDateTime dateTime, String type, String department) {
        if (dateTime.isBefore(currentDateTime)) return false;

        Appointment appt = new Appointment(dateTime, type, department);
        appointments.add(appt);

        // Schedule Reminder (1 Day Before)
        scheduleEvent(dateTime.minusDays(1), () ->
                System.out.println("Reminder: \"" + type + "\" at " + department + " happens tomorrow."));

        // Schedule "Now" Notification
        scheduleEvent(dateTime, () ->
                System.out.println("Appointment NOW: " + type + " (" + department + ")"));

        return true;
    }

    public void removeAppointment(Appointment appt) {
        appointments.remove(appt);
    }

    /**
     * Internal record to hold queued simulation events.
     * Comparable implementation ensures PriorityQueue orders them by time.
     */
    private record ScheduledEvent(LocalDateTime dateTime, Runnable action)
            implements Comparable<ScheduledEvent> {

        @Override
        public int compareTo(ScheduledEvent o) {
            return this.dateTime.compareTo(o.dateTime);
        }
    }

    // ==========================================
    // 4. TEST DATA INITIALIZATION
    // ==========================================

    /**
     * Populates the model with dummy data for demonstration purposes.
     */
    public void loadSampleData() {
        // Future
        appointments.add(new Appointment(currentDateTime.plusDays(2), "Consultation", "Cardiology"));
        appointments.add(new Appointment(currentDateTime.plusDays(5), "Dental Cleaning", "Dentistry"));
        appointments.add(new Appointment(currentDateTime.plusDays(8), "Eye Checkup", "Ophthalmology"));

        // Past
        appointmentHistory.add(new Appointment(currentDateTime.minusDays(3), "Vaccination", "General Medicine"));
        appointmentHistory.add(new Appointment(currentDateTime.minusDays(15), "Blood Work", "Laboratory"));
        
        // User Profile
        userProfile.setName("John Doe");
        userProfile.setEmail("john.doe@example.com");
        userProfile.setPhoneNumber("+123456789");
        userProfile.setInsurance(InsuranceLevel.STANDARD);
        userProfile.setAccountType(AccountType.PRIMARY_USER);
    }
}