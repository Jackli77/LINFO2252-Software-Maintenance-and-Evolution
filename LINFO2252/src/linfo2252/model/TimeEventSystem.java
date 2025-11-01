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
        model.getLogger().log("Time advanced to " + currentDate);
        triggerEvents();
    }

    private void triggerEvents(){
        // Example event
        if(Math.random() < 0.2) {
            model.getLogger().log("Doctor became unavailable!");
            // modify appointments through model
        }
    }

    public LocalDate getDate(){
        return currentDate;
    }
}

