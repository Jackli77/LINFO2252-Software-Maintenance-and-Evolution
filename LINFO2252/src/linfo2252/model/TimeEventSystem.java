package linfo2252.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TimeEventSystem {
    private LocalDate currentDate = LocalDate.now();
    private final Model model;
    private final List<TimeEvent> events = new ArrayList<>();

    public TimeEventSystem(Model model){
        this.model = model;
    }

    public void advanceDays(int days){
        currentDate = currentDate.plusDays(days);
        triggerEvents();
    }

    public void addEvent(TimeEvent event){
        events.add(event);
    }

    private void triggerEvents(){
        Iterator<TimeEvent> it = events.iterator();
        while (it.hasNext()) {
            TimeEvent event = it.next();
            if (!event.getTriggerDate().isAfter(currentDate)) {
                event.execute(model);
                it.remove(); // remove after trigger
            }
        }
    }

    public LocalDate getDate(){
        return currentDate;
    }
}
