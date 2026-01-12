package linfo2252.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the discrete event simulation timeline.
 * <p>
 * This system maintains the simulated "current date" and a queue of pending {@link TimeEvent} objects.
 * When time advances, it identifies and executes all events scheduled for the current or past dates,
 * removing them from the queue after execution.
 */
public class TimeEventSystem {
    
    // The current date within the simulation context
    private LocalDate currentDate = LocalDate.now();
    
    // Reference to the main model (needed to execute events against system state)
    private final Model model;
    
    // List of pending events waiting to be triggered
    private final List<TimeEvent> events = new ArrayList<>();

    /**
     * Constructs a new simulation engine.
     *
     * @param model The core system model that events will interact with.
     */
    public TimeEventSystem(Model model){
        this.model = model;
    }

    /**
     * Advances the simulation clock by a specified number of days.
     * Automatically triggers any events that fall due within the new timeframe.
     *
     * @param days The number of days to move forward (must be positive).
     */
    public void advanceDays(int days){
        currentDate = currentDate.plusDays(days);
        triggerEvents();
    }

    /**
     * Schedules a new event for future execution.
     *
     * @param event The event object containing the trigger date and execution logic.
     */
    public void addEvent(TimeEvent event){
        events.add(event);
    }

    /**
     * Iterates through all pending events and executes those whose trigger date
     * has been reached or passed. Executed events are removed from the queue.
     */
    private void triggerEvents(){
        Iterator<TimeEvent> it = events.iterator();
        while (it.hasNext()) {
            TimeEvent event = it.next();
            // If the event is today or in the past...
            if (!event.getTriggerDate().isAfter(currentDate)) {
                event.execute(model);
                it.remove(); // Clean up processed events
            }
        }
    }

    public LocalDate getDate(){
        return currentDate;
    }
}