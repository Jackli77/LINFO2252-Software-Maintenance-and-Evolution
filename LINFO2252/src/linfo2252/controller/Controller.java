package linfo2252.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import linfo2252.model.Appointment;
import linfo2252.model.Model;
import linfo2252.view.ConsoleView;
import linfo2252.view.MainView;

public class Controller implements ControllerInterface {

    private final Model model;
    private final ConsoleView consoleView;

    // Swing UI (optional)
    private MainView mainView;

    private boolean uiEnabled = true;

    public Controller() {
        this.model = new Model();
        this.consoleView = new ConsoleView(model.getFeatureManager());
        model.loadSampleData();
    }

    public Model getModel() { return model; }

    @Override
    public int activate(String[] deactivations, String[] activations ){
        if( deactivations != null ){
            for( String name : deactivations ){
                if( !model.getFeatureManager().getAvailableFeatures().contains(name)){
                    System.out.println("Feature not found: " + name);
                    return 1;
                }
                model.getFeatureManager().deactivate(name);
            }
        }

        if( activations != null ){
            for( String name : activations ){
                if( !model.getFeatureManager().getAvailableFeatures().contains(name)){
                    System.out.println("Feature not found: " + name);
                    return 1;
                }
                model.getFeatureManager().activate(name);
            }
        }
        return 0;
    }

    @Override
    public boolean enableUIView(){
        if(uiEnabled){
            System.out.println("UI already enabled.");
            return true;
        }
        uiEnabled = true;
        System.out.println("UI enabled.");
        return true;
    }

    @Override
    public boolean disableUIView(){
        if(!uiEnabled){
            System.out.println("UI already disabled.");
            return true;
        }
        uiEnabled = false;
        System.out.println("UI disabled.");
        return true;
    }

    @Override
    public String[] getStateAsLog(){
        return model.getFeatureManager().getLogs().toArray(new String[0]);
    }

    public void start(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Smart Medical Appointment Manager (Console Version)");

        while(true){
            System.out.print("> ");
            String input = sc.nextLine().trim();
            if(input.equalsIgnoreCase("exit")) break;

            if(input.equalsIgnoreCase("next day")) {
                model.advanceOneDay();
            }
            else if(input.equalsIgnoreCase("show date")) {
                System.out.println("Current date: " + model.getCurrentDate());
            }
            else if(input.equalsIgnoreCase("show logs")){
                consoleView.showLogs();
            }
            else {
                System.out.println("Unknown command.");
            }
        }
        sc.close();
    }

    // ========== UI Action Handlers (for Swing) ========== //

    public void setMainView(MainView v){ this.mainView = v; }
    public MainView getMainView(){ return mainView; }

    public void onAddAppointment(LocalDateTime dateTime, String type, String department) {
        model.createAppointment(dateTime, type, department);
        if(mainView != null) mainView.updateAppointmentView(model.getAppointments());
    }

    public void onRemoveAppointment(Appointment appt) {
        model.removeAppointment(appt);
        if(mainView != null) mainView.updateAppointmentView(model.getAppointments());
    }
    
    public void onViewHistory() {
        if(mainView != null)
            mainView.updateHistoryView(model.getAppointmentHistory());
    }
    
    public void onAdvanceDay() {
        model.advanceOneDay();
        if(mainView != null) {
            mainView.updateAppointmentView(model.getAppointments());
            mainView.updateHistoryView(model.getAppointmentHistory());
            mainView.updateDateDisplay(model.getCurrentDate());
        }
    }
    
    public void onAdvanceWeek() {
        model.advanceOneWeek();
        if(mainView != null) {
            mainView.updateAppointmentView(model.getAppointments());
            mainView.updateHistoryView(model.getAppointmentHistory());
            mainView.updateDateDisplay(model.getCurrentDate());
        }
    }



}
