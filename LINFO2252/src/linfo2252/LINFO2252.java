package linfo2252;

import linfo2252.controller.Controller;
import linfo2252.model.Model;
import linfo2252.view.ConsoleView;

public class LINFO2252 {
    public static void main(String[] args) {
        Model model = new Model();
        model.loadSampleData();
        Controller controller = new Controller(model);
        controller.enableUIView(); 
        ConsoleView console = new ConsoleView(controller);
        console.start();
    }
}