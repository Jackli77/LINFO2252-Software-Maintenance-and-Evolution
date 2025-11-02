package linfo2252.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Model {

    private LocalDate currentDate;
    private PriorityQueue<ScheduledEvent> eventQueue;
    private List<Appointment> appointments;
    private FeatureManager featureManager;

    public Model() {
        this.currentDate = LocalDate.now();
        this.eventQueue = new PriorityQueue<>();
        this.appointments = new ArrayList<>();
        this.featureManager = new FeatureManager();
        this.featureManager.registerFeature(new UserFeature());
        this.featureManager.registerFeature(new AppointmentFeature());
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    // ========== TIME EVENT SIMULATOR ========== //

    public void advanceOneDay() {
        currentDate = currentDate.plusDays(1);
        runDueEvents();
    }

    private void runDueEvents() {
        while (!eventQueue.isEmpty() && !eventQueue.peek().date().isAfter(currentDate)) {
            ScheduledEvent ev = eventQueue.poll();
            ev.action().run();
        }
    }

    public void scheduleEvent(LocalDate date, Runnable action) {
        eventQueue.add(new ScheduledEvent(date, action));
    }

    // ========== APPOINTMENTS ========== //

    public void createAppointment(LocalDateTime dateTime, String type, String department) {
        Appointment appt = new Appointment(dateTime,type,department);
        appointments.add(appt);

        // Automatically schedule reminder event
        scheduleEvent(appt.getDate().minusDays(1), () ->
                System.out.println("Reminder: appointment \"" + type + " at " + department + "\" is tomorrow."));

        scheduleEvent(appt.getDate(), () ->
                System.out.println("Appointment today: " + type + " at " + department));
    }

    public void removeAppointment(Appointment appt) {
        appointments.remove(appt);
    }

    // ========== RECORD CLASS FOR EVENTS ========== //
    private record ScheduledEvent(LocalDate date, Runnable action) implements Comparable<ScheduledEvent> {
        @Override
        public int compareTo(ScheduledEvent o) {
            return this.date.compareTo(o.date);
        }
    }
}
