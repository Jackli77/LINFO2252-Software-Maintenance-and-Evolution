package linfo2252;

import linfo2252.controller.Controller;
import linfo2252.model.Model;
import linfo2252.view.ConsoleView;

public class LINFO2252 {
    public static void main(String[] args) {
        // 1. Create the Model
        Model model = new Model();

        // 2. CRITICAL: Load the sample data immediately!
        // If you don't do this, your lists are empty when the View starts.
        model.loadSampleData();

        // 3. Create the Controller
        Controller controller = new Controller(model);

        // 4. Start the UI
        // This calls 'new MainView(controller)', which grabs the data from the model
        controller.enableUIView(); 
        
        // 5. (Optional) Start Console View in parallel
        ConsoleView console = new ConsoleView(controller);
        console.start();
    }
}