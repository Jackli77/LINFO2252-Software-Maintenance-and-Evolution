package linfo2252.model;

import java.time.LocalDate;

public interface TimeEvent {
    LocalDate getTriggerDate();
    void execute(Model model);
}
