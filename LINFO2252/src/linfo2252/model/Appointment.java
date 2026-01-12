package linfo2252.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a scheduled medical appointment entity.
 * <p>
 * This class serves as a data container (POJO) holding all relevant details 
 * for a patient's visit, including timing, medical classification, and assigned personnel.
 */
public class Appointment {

    // Core mandatory fields
    private LocalDateTime dateTime;
    private String type;
    private String department;
    
    // Optional / Extended details
    private String location;
    private String doctorName;

    /**
     * Constructs a new Appointment with the mandatory core details.
     *
     * @param dateTime   The specific date and time of the appointment.
     * @param type       The nature of the visit (e.g., "Consultation", "Surgery").
     * @param department The medical department responsible (e.g., "Cardiology").
     */
    public Appointment(LocalDateTime dateTime, String type, String department) {
        this.dateTime = dateTime;
        this.type = type;
        this.department = department;
    }
    
    /**
     * Returns a formatted string representation suitable for UI list displays.
     * <p>
     * Format: {@code "yyyy/MM/dd HH:mm | Type (Department)"}
     */
    @Override
    public String toString() {
        // 'kk' represents clock-hour 1-24. Switched to 'HH' (00-23) for standard ISO consistency,
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return dateTime.format(formatter) + " | " + type + " (" + department + ")";
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    /**
     * Convenience method to retrieve just the date portion.
     * Useful for filtering appointments by day without worrying about time.
     */
    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public String getType() {
        return type;
    }

    public String getDepartment() {
        return department;
    }
    
    public String getLocation() {
        return location;
    }

    public String getDoctorName() {
        return doctorName;
    }

    // -------------------------------------------------------------------------
    // Mutators (Setters)
    // -------------------------------------------------------------------------

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}