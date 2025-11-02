package linfo2252;

import java.time.LocalDate;

import linfo2252.controller.UIController;
import linfo2252.model.Model;
import linfo2252.model.TimeEvent;
import linfo2252.model.TimeEventSystem;
import linfo2252.view.UIView;

public class LINFO2252 {
	public static void main(String[] args) {
	    Model model = new Model();
	    UIController controller = new UIController(model);
	    UIView view = new UIView(controller);

	    controller.start(); // optional if you still want the console commands
	}
}
