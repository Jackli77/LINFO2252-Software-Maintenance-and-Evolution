package linfo2252;

import linfo2252.controller.Controller;
import linfo2252.model.Model;
import linfo2252.view.MainView;

public class LINFO2252 {
	public static void main(String[] args) {
	    Model model = new Model();
	    Controller controller = new Controller();
	    MainView view = new MainView(controller);
	}
}
