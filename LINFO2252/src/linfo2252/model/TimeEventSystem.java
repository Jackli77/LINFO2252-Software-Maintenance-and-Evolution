package linfo2252.model;

import java.time.LocalDate;

public class TimeEventSystem {
    private LocalDate currentDate = LocalDate.now();
    private final Model model;

    public TimeEventSystem(Model model){
        this.model = model;
    }

    public void advanceDays(int days){
        currentDate = currentDate.plusDays(days);
        triggerEvents();
    }

    private void triggerEvents(){
        if(Math.random() < 0.2) {     
        }
    }

    public LocalDate getDate(){
        return currentDate;
    }
}

