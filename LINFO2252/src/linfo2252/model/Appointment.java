package linfo2252.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Appointment {
    private LocalDateTime dateTime;
    private String type;
    private String department;
    
    // Optional features
    private String location;
    private String doctorName;

    public Appointment(LocalDateTime dateTime, String type, String department) {
        this.dateTime = dateTime;
        this.type = type;
        this.department = department;
    }
    
    @Override
    public String toString() {
        return dateTime.toString() + " | " + type + " (" + department + ")";
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public LocalDate getDate() {
    	return dateTime.toLocalDate();
    }

    public String getType() {
        return type;
    }

    public String getDepartment() {
        return department;
    }

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

    public String getLocation() {
        return location;
    }

    public String getDoctorName() {
        return doctorName;
    }
}
