package linfo2252.model;

import java.time.LocalDate;

/**
 * Represents a significant event in a patient's medical history.
 * <p>
 * This class is a lightweight Data Transfer Object (DTO) used to store 
 * immutable records of past medical interactions or conditions.
 */
public class MedicalHistoryEvent {
    
    // Public fields for direct access (common in simple DTOs)
	public LocalDate date;
    public String info;

    /**
     * Constructs a new medical history record.
     *
     * @param date    The date when the event occurred.
     * @param details (Deprecated) Additional details parameter, currently unused.
     * @param info    The primary description of the medical event.
     */
    public MedicalHistoryEvent(LocalDate date, String details, String info) {
        this.date = date; 
        this.info = info;
        // Note: 'details' parameter is present in constructor signature but not stored.
        // Consider removing if not needed for future extensibility.
    }
}