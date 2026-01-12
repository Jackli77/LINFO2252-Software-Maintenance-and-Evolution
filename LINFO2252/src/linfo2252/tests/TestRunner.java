package linfo2252.tests;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;
import linfo2252.model.Model;
import java.time.LocalDateTime;

public class TestRunner {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        System.out.println("Running Automated System Tests...\n");

        runTest("Feature Gating Test", TestRunner::testFeatureBlocking);
        runTest("Appointment Flow Test", TestRunner::testAppointmentCreation);
        runTest("Time Simulation Test", TestRunner::testTimeSimulation);
        
        System.out.println("\nAll Tests Completed.");
    }

    private static void testFeatureBlocking() {
        Model model = new Model();
        Controller controller = new Controller(model);
        
        // 1. Deactivate the feature
        controller.activate(new String[]{"AppointmentManagement"}, null);
        
        int initialSize = model.getAppointments().size();
        controller.addAppointment(LocalDateTime.now().plusDays(1), "Test", "Test");
        
        assertState(model.getAppointments().size() == initialSize, 
            "Appointment list size should remain " + initialSize + " but was " + model.getAppointments().size());
    }

    private static void testAppointmentCreation() {
        Model model = new Model();
        Controller controller = new Controller(model);
        
        // 1. Ensure feature is active
        controller.activate(null, new String[]{"AppointmentManagement"});
        
        // 2. Add Appointment
        int initialSize = model.getAppointments().size();
        controller.addAppointment(LocalDateTime.now().plusDays(1), "Checkup", "Cardio");
        
        // 3. Verify Model Updated
        assertState(model.getAppointments().size() == initialSize + 1, "Appointment list size should increase by 1");
        
        // 4. Verify Logs generated
        String[] logs = controller.getStateAsLog();
        
        boolean foundAction = false;
        for (String line : logs) {
            if (line.contains("lastAction") && line.contains("AddAppointment")) {
                foundAction = true;
                break;
            }
        }
        
        assertState(foundAction, "JSON Log should contain 'lastAction': 'AddAppointment'");
    }

    private static void testTimeSimulation() {
        Model model = new Model();
        Controller controller = new Controller(model);
        controller.activate(null, new String[]{"TimeSimulation", "AppointmentManagement"});

        LocalDateTime tomorrow = model.getCurrentDateTime().plusDays(1).withHour(12);
        controller.addAppointment(tomorrow, "FutureEvent", "General");
        
        Appointment createdAppt = model.getAppointments().get(model.getAppointments().size() - 1);

        controller.advanceDays(2);
        
        boolean inActive = model.getAppointments().contains(createdAppt);
        assertState(!inActive, "Appointment should be removed from active list after date passes");
        
        boolean inHistory = model.getAppointmentHistory().contains(createdAppt);
        assertState(inHistory, "Appointment should appear in history list");
    }

    private static void runTest(String name, Runnable test) {
        System.out.print("TEST: " + name + "... ");
        try {
            test.run();
            System.out.println(ANSI_GREEN + "PASS" + ANSI_RESET);
        } catch (RuntimeException e) {
            System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);
            System.out.println("   Reason: " + e.getMessage());
        }
    }

    private static void assertState(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }
}