package linfo2252.view;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;
import linfo2252.model.FeatureManager;
import linfo2252.observer.Observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements Observer, Runnable {

    private final Controller controller;
    private final FeatureManager featureManager;
    private final Scanner scanner;
    private boolean running = true;

    public ConsoleView(Controller controller) {
        this.controller = controller;
        this.featureManager = controller.getModel().getFeatureManager();
        this.featureManager.addObserver(this);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Welcome to Smart Medical Manager (Console Mode)");
        while (running) {
            printMainMenu();
            System.out.print("> ");
            handleMainInput(scanner.nextLine().trim());
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
    }

    // ================= MAIN MENU =================

    private void printMainMenu() {
        System.out.println("\n===== MAIN SYSTEM MENU =====");
        System.out.println(" 1. Show System State / Logs");
        System.out.println(" 2. Activate Feature");
        System.out.println(" 3. Deactivate Feature");
        System.out.println(" 4. Enable GUI");
        System.out.println(" 5. Disable GUI");
        System.out.println(" 6. OPEN CONSOLE APP (Closes GUI) >>"); 
        System.out.println(" 7. Exit");
        System.out.println("============================");
    }

    private void handleMainInput(String input) {
        switch (input) {
            case "1" -> showLogs();
            case "2" -> toggleFeature(true);
            case "3" -> toggleFeature(false);
            case "4" -> { 
                controller.enableUIView(); 
                System.out.println(">> GUI Enabled."); 
            }
            case "5" -> { 
                controller.disableUIView(); 
                System.out.println(">> GUI Disabled."); 
            }
            case "6" -> {
                System.out.println(">> Switching to Console App Mode...");
                controller.disableUIView(); 
                showAppRootMenu(); 
            }
            case "7" -> {
                System.out.println("Exiting...");
                running = false;
                System.exit(0);
            }
            default -> System.out.println("Invalid command.");
        }
    }

    // ================= LEVEL 1: APP ROOT MENU =================

    private void showAppRootMenu() {
        boolean inApp = true;
        while (inApp) {
            LocalDateTime now = controller.getModel().getCurrentDateTime();
            System.out.println("\n===== APPLICATION DASHBOARD [ " + now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")) + " ] =====");
            
            System.out.println(" 1. Manage Appointments " + getLockStatus("AppointmentManagement"));
            System.out.println(" 2. View History " + getLockStatus("HistoryTracking"));
            System.out.println(" 3. User Profile Settings " + getLockStatus("UserProfile"));
            System.out.println(" 4. Time Controls " + getLockStatus("TimeSimulation"));
            System.out.println(" 0. Back to System Menu");
            System.out.print("app> ");

            switch (scanner.nextLine().trim()) {
                case "1" -> openAppointmentSubmenu();
                case "2" -> openHistorySubmenu();
                case "3" -> openProfileSubmenu();
                case "4" -> openTimeSubmenu();
                case "0" -> inApp = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ================= LEVEL 2: SPECIFIC SUBMENUS =================

    // --- 1. APPOINTMENTS ---
    private void openAppointmentSubmenu() {
        if (!checkFeature("AppointmentManagement")) return;

        boolean inMenu = true;
        while (inMenu) {
            List<Appointment> list = controller.getModel().getAppointments();
            System.out.println("\n--- APPOINTMENT MANAGER ---");
            System.out.println(" [Current List]: " + (list.isEmpty() ? "Empty" : list.size() + " scheduled"));
            System.out.println(" 1. List All Details");
            System.out.println(" 2. Add New (Manual)");
            System.out.println(" 3. Add New (Random Event)");
            System.out.println(" 4. Remove Appointment");
            System.out.println(" 0. Back");
            System.out.print("app/appointments> ");

            switch (scanner.nextLine().trim()) {
                case "1" -> listAppointments(list);
                case "2" -> addManualAppointment();
                case "3" -> {
                    controller.createRandomAppointment();
                    System.out.println(">> Random appointment generated.");
                }
                case "4" -> removeAppointmentWizard();
                case "0" -> inMenu = false;
            }
        }
    }

    // --- 2. HISTORY ---
    private void openHistorySubmenu() {
        if (!checkFeature("HistoryTracking")) return;

        System.out.println("\n--- HISTORY ARCHIVE ---");
        List<Appointment> list = controller.getModel().getAppointmentHistory();
        if (list.isEmpty()) System.out.println("No past records found.");
        else listAppointments(list);
        
        System.out.println("\n(Press Enter to return)");
        scanner.nextLine();
    }

    // --- 3. PROFILE ---
    private void openProfileSubmenu() {
        if (!checkFeature("UserProfile")) return;

        boolean inMenu = true;
        while (inMenu) {
            var user = controller.getModel().getUserProfile();
            String badge = (user.getInsurance() == linfo2252.model.InsuranceLevel.PREMIUM) ? " [GOLD MEMBER]" : "";

            System.out.println("\n--- USER PROFILE ---");
            System.out.println(" Name:      " + user.getName());
            System.out.println(" Email:     " + user.getEmail());
            System.out.println(" Phone:     " + user.getPhoneNumber());
            System.out.println(" Plan:      " + user.getInsurance() + badge);
            System.out.println("--------------------");
            System.out.println(" 1. Edit Name");
            System.out.println(" 2. Edit Email");
            System.out.println(" 3. Toggle Insurance Plan");
            System.out.println(" 0. Back");
            System.out.print("app/profile> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("New Name: ");
                    controller.updateUserProfile(scanner.nextLine(), user.getEmail(), user.getPhoneNumber(), user.getInsurance());
                }
                case "2" -> {
                    System.out.print("New Email: ");
                    controller.updateUserProfile(user.getName(), scanner.nextLine(), user.getPhoneNumber(), user.getInsurance());
                }
                case "3" -> {
                    var newLevel = (user.getInsurance() == linfo2252.model.InsuranceLevel.STANDARD) 
                            ? linfo2252.model.InsuranceLevel.PREMIUM 
                            : linfo2252.model.InsuranceLevel.STANDARD;
                    controller.updateUserProfile(user.getName(), user.getEmail(), user.getPhoneNumber(), newLevel);
                    System.out.println(">> Plan changed to " + newLevel);
                }
                case "0" -> inMenu = false;
            }
        }
    }

    // --- 4. TIME ---
    private void openTimeSubmenu() {
        if (!checkFeature("TimeSimulation")) return;

        boolean inMenu = true;
        while (inMenu) {
            LocalDateTime now = controller.getModel().getCurrentDateTime();
            System.out.println("\n--- TIME CONTROL ---");
            System.out.println(" Current Simulated Date: " + now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
            System.out.println(" 1. Advance 1 Day");
            System.out.println(" 2. Advance 1 Week");
            System.out.println(" 0. Back");
            System.out.print("app/time> ");

            switch (scanner.nextLine().trim()) {
                case "1" -> { controller.advanceDays(1); System.out.println(">> +1 Day"); }
                case "2" -> { controller.advanceDays(7); System.out.println(">> +1 Week"); }
                case "0" -> inMenu = false;
            }
        }
    }

    // ================= HELPERS =================

    private boolean checkFeature(String name) {
        if (controller.isFeatureActive(name)) return true;
        System.out.println(">> [!] Feature LOCKED: " + name + " is disabled.");
        return false;
    }

    private String getLockStatus(String name) {
        return controller.isFeatureActive(name) ? "" : "(LOCKED)";
    }

    private void listAppointments(List<Appointment> list) {
        if (list.isEmpty()) System.out.println(" (List is empty)");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf(" %d. %s\n", (i + 1), list.get(i));
        }
    }

    private void addManualAppointment() {
        try {
            System.out.print("Days from now: ");
            int days = Integer.parseInt(scanner.nextLine());
            System.out.print("Hour (0-23): ");
            int hour = Integer.parseInt(scanner.nextLine());
            System.out.print("Type: ");
            String type = scanner.nextLine();
            System.out.print("Department: ");
            String dept = scanner.nextLine();

            LocalDateTime date = controller.getModel().getCurrentDateTime()
                    .plusDays(days).withHour(hour).withMinute(0);
            controller.addAppointment(date, type, dept);
            System.out.println(">> Added.");
        } catch (Exception e) { System.out.println(">> Invalid input."); }
    }

    private void removeAppointmentWizard() {
        List<Appointment> list = controller.getModel().getAppointments();
        listAppointments(list);
        if (list.isEmpty()) return;
        System.out.print("Number to remove: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx > 0 && idx <= list.size()) controller.removeAppointment(list.get(idx - 1));
        } catch (Exception e) { System.out.println(">> Invalid."); }
    }

    private void showLogs() {
        String[] logs = controller.getStateAsLog();
        System.out.println("\n[CURRENT STATE SNAPSHOT]");
        for (String line : logs) System.out.println(line);
    }

    private void toggleFeature(boolean enable) {
        List<String> allFeatures = new ArrayList<>(featureManager.getAvailableFeatures());
        Collections.sort(allFeatures);

        System.out.println("\n--- Select Feature to " + (enable ? "ACTIVATE" : "DEACTIVATE") + " ---");
        for (int i = 0; i < allFeatures.size(); i++) {
            String name = allFeatures.get(i);
            String status = featureManager.getFeature(name).isActive() ? "[ACTIVE]" : "[INACTIVE]";
            System.out.printf(" %d. %-20s %s%n", (i + 1), name, status);
        }
        System.out.println(" 0. Cancel");
        System.out.print("Choice: ");

        try {
            int c = Integer.parseInt(scanner.nextLine()) - 1;
            if (c >= 0 && c < allFeatures.size()) {
                String[] target = {allFeatures.get(c)};
                if (enable) controller.activate(null, target);
                else controller.activate(target, null);
                System.out.println(">> State Changed.");
            }
        } catch (Exception e) {
            System.out.println(">> Invalid Input.");
        }
    }

    @Override
    public void update() {}
}