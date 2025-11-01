package linfo2252;

import linfo2252.controller.UIController;
import linfo2252.model.Model;
import linfo2252.view.UIView;
import linfo2252.logger.UILogger;

public class LINFO2252 {
    public static void main(String[] args) {
        // Create the view
        UIView view = new UIView();

        // Create the logger (connects model <-> view)
        UILogger logger = new UILogger(view);

        // Create the model (receives logger)
        Model model = new Model(logger);

        // Create the controller
        UIController controller = new UIController(model);

        // Start controller
        controller.start();
    }
}
