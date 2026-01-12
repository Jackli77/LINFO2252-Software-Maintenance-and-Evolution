package linfo2252.model;

import java.time.LocalDate;

/**
 * Defines the contract for time-triggered simulation events.
 * <p>
 * This interface follows the <b>Command Pattern</b>. Implementations encapsulate
 * specific logic (the 'command') that must be executed when the simulation clock 
 * reaches a specific trigger date.
 */
public interface TimeEvent {

    /**
     * Retrieves the scheduled date for this event.
     * The simulation engine uses this to order events in the priority queue.
     *
     * @return The date when the event logic should be triggered.
     */
    LocalDate getTriggerDate();

    /**
     * Executes the business logic associated with this event.
     *
     * @param model The core system model, provided as context to allow the event 
     * to modify system state (e.g., adding appointments, sending logs).
     */
    void execute(Model model);
}