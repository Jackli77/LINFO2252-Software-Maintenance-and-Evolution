package linfo2252.controller;

import java.util.Scanner;
import linfo2252.model.Model;

public class UIController {
    private final Model model;

    public UIController(Model model) {
        this.model = model;
    }

    public void start() {
        Scanner in = new Scanner(System.in);
        System.out.println("Commands: day, week, stop");
        while (true) {
            String cmd = in.nextLine();
            switch (cmd) {
                case "day":
                    model.getTES().advanceDays(1);
                    break;
                case "week":
                    model.getTES().advanceDays(7);
                    break;
                case "stop":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command: " + cmd);
            }
        }
    }
}
