package linfo2252;

import linfo2252.controller.Controller;
import linfo2252.model.Model;
import linfo2252.view.ConsoleView;

/**
 * The main entry point for the Smart Medical Appointment Manager application.
 * <p>
 * This class is responsible for bootstrapping the MVC architecture, 
 * loading initial simulation data, and launching both the Graphical User Interface (GUI)
 * and the Console Interface in parallel.
 */
public class LINFO2252 {

    public static void main(String[] args) {
        // 1. Initialize the Core Model and load simulation data
        Model model = new Model();
        model.loadSampleData();

        // 2. Initialize the Controller (Mediator)
        Controller controller = new Controller(model);

        // 3. Launch the Graphical User Interface (Swing)
        // This sets up the MainView and registers it as an observer
        controller.enableUIView(); 
        
        // 4. Start the Console Interface (Parallel Thread)
        // Allows the user to control the application via terminal simultaneously
        ConsoleView console = new ConsoleView(controller);
        console.start();
    }
}