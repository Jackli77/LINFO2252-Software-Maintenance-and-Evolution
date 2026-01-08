package linfo2252.view;

import linfo2252.controller.Controller;
import linfo2252.model.features.FeatureManager;
import linfo2252.observer.Observer;

import java.util.Scanner;

public class ConsoleView implements Observer, Runnable {

    private final Controller controller;
    private final FeatureManager featureManager;
    private boolean running = true;

    public ConsoleView(Controller controller) {
        this.controller = controller;
        // We get the feature manager to observe it (Passive View for reading state)
        this.featureManager = controller.getModel().getFeatureManager();
        this.featureManager.addObserver(this);
    }

    /**
     * Starts the interactive console on a separate thread
     * so it doesn't block the Swing GUI if both are running.
     */
    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Smart Medical Manager (Console Mode)");
        
        while (running) {
            printMenu();
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            handleInput(input);
            
            // Small delay to keep UI clean
            try { Thread.sleep(200); } catch (InterruptedException e) {}
        }
    }

    private void printMenu() {
        System.out.println("\n--------------------------------");
        System.out.println(" 1. Show System State / Logs");
        System.out.println(" 2. Activate Feature");
        System.out.println(" 3. Deactivate Feature");
        System.out.println(" 4. Enable GUI");
        System.out.println(" 5. Disable GUI");
        System.out.println(" 6. Exit");
        System.out.println("--------------------------------");
    }

    private void handleInput(String input) {
        switch (input) {
            case "1":
                showLogs();
                break;
            case "2":
                toggleFeature(true);
                break;
            case "3":
                toggleFeature(false);
                break;
            case "4":
                controller.enableUIView();
                System.out.println(">> GUI Enabled.");
                break;
            case "5":
                controller.disableUIView();
                System.out.println(">> GUI Disabled.");
                break;
            case "6":
                System.out.println("Exiting Console...");
                running = false;
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void showLogs() {
        // We use the Controller interface method we built earlier
        String[] logs = controller.getStateAsLog();
        System.out.println("\n[CURRENT STATE SNAPSHOT]");
        for (String line : logs) {
            System.out.println(line);
        }
    }

    private void toggleFeature(boolean enable) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Available Features: " + featureManager.getAvailableFeatures());
        System.out.print("Enter feature name: ");
        String name = sc.nextLine();

        // Use the Controller's standard activate interface
        // activate(deactivations[], activations[])
        String[] target = new String[]{name};
        
        int result;
        if (enable) {
            result = controller.activate(null, target); // Activate
        } else {
            result = controller.activate(target, null); // Deactivate
        }

        if (result == 0) {
            System.out.println(">> Success: " + name + (enable ? " Activated" : " Deactivated"));
        } else {
            System.out.println(">> Error: Feature not found or invalid.");
        }
    }

    // --- Observer Implementation ---
    // This triggers automatically if you click buttons in the Swing GUI
    @Override
    public void update() {
        // We print a subtle notification instead of re-printing the whole menu
        // to avoid disrupting the user if they are typing.
        System.out.print("\n [!] System State Updated (Features Changed) [!]\n> ");
    }
}