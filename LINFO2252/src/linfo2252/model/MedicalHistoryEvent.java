package linfo2252.model;

import java.time.LocalDate;

public class MedicalHistoryEvent {
	public LocalDate date;
    public String info;

    public MedicalHistoryEvent(LocalDate date, String details, String info) {
        this.date = date; this.info = info;
    }
}
