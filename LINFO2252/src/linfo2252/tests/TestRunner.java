package linfo2252.tests;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;
import linfo2252.model.Model;
import java.time.LocalDateTime;

/**
 * A lightweight, self-contained integration test suite.
 * <p>
 * This class validates the core architectural constraints of the system, such as 
 * feature gating and temporal consistency. It runs without external dependencies (like JUnit),
 * fulfilling the requirement for a robust, standalone application.
 */
public class TestRunner {

    // Console Color Codes for better visibility of Test Results
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * Entry point for running the automated verification suite.
     */
    public static void main(String[] args) {
        System.out.println("Running Automated System Tests...\n");

        runTest("Feature Gating Test", TestRunner::testFeatureBlocking);
        runTest("Appointment Flow Test", TestRunner::testAppointmentCreation);
        runTest("Time Simulation Test", TestRunner::testTimeSimulation);
        
        System.out.println("\nAll Tests Completed.");
    }

    // -------------------------------------------------------------------------
    // Test Cases
    // -------------------------------------------------------------------------

    /**
     * TC-01: Verifies that deactivating a feature strictly blocks its associated operations.
     * Scenario: User tries to add an appointment while 'AppointmentManagement' is disabled.
     * Expected: The model state remains unchanged.
     */
    private static void testFeatureBlocking() {
        Model model = new Model();
        Controller controller = new Controller(model);
        
        // 1. Deactivate the feature
        controller.activate(new String[]{"AppointmentManagement"}, null);
        
        // 2. Attempt to perform the blocked action
        int initialSize = model.getAppointments().size();
        controller.addAppointment(LocalDateTime.now().plusDays(1), "Test", "Test");
        
        // 3. Verify that the system rejected the input
        assertState(model.getAppointments().size() == initialSize, 
            "Appointment list size should remain " + initialSize + " but was " + model.getAppointments().size());
    }

    /**
     * TC-02: Verifies the standard success flow for creating data.
     * Scenario: User adds a valid appointment with features enabled.
     * Expected: Model updates correctly and the action is logged to the system state.
     */
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
        
        // 4. Verify Logs generated (Audit Trail check)
        String[] logs = controller.getStateAsLog();
        
        boolean foundAction = false;
        for (String line : logs) {
            // Check if the log JSON contains the expected action key-value pair
            if (line.contains("lastAction") && line.contains("AddAppointment")) {
                foundAction = true;
                break;
            }
        }
        
        assertState(foundAction, "JSON Log should contain 'lastAction': 'AddAppointment'");
    }

    /**
     * TC-03: Verifies temporal logic and state transitions.
     * Scenario: Time is advanced past an appointment's scheduled date.
     * Expected: Appointment moves from the 'Active' list to the 'History' archive.
     */
    private static void testTimeSimulation() {
        Model model = new Model();
        Controller controller = new Controller(model);
        controller.activate(null, new String[]{"TimeSimulation", "AppointmentManagement"});

        // 1. Schedule future event
        LocalDateTime tomorrow = model.getCurrentDateTime().plusDays(1).withHour(12);
        controller.addAppointment(tomorrow, "FutureEvent", "General");
        
        Appointment createdAppt = model.getAppointments().get(model.getAppointments().size() - 1);

        // 2. Advance time (Simulate 2 days passing)
        controller.advanceDays(2);
        
        // 3. Verify it is GONE from active appointments
        boolean inActive = model.getAppointments().contains(createdAppt);
        assertState(!inActive, "Appointment should be removed from active list after date passes");
        
        // 4. Verify it exists in HISTORY
        boolean inHistory = model.getAppointmentHistory().contains(createdAppt);
        assertState(inHistory, "Appointment should appear in history list");
    }

    // -------------------------------------------------------------------------
    // Test Infrastructure Helpers
    // -------------------------------------------------------------------------

    /**
     * Executes a single test case and reports the result to the console.
     *
     * @param name The human-readable name of the test.
     * @param test The test logic to execute.
     */
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

    /**
     * Validates a boolean condition. Throws a RuntimeException if false.
     * Acts as a lightweight 'JUnit Assert'.
     */
    private static void assertState(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }
}