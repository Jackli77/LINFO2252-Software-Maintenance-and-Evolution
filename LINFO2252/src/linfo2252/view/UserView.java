package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import linfo2252.controller.Controller;
import linfo2252.model.UserProfile;

public class UserView extends JPanel {

    private Controller controller;
    private JLabel userNameLabel;
    private JLabel insuranceLabel;
    private JLabel accountTypeLabel;

    public UserView(Controller controller) {
        this.controller = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initUI();
    }

    private void initUI() {
        userNameLabel = new JLabel("User: ");
        insuranceLabel = new JLabel("Insurance: ");
        accountTypeLabel = new JLabel("Account Type: ");

        add(userNameLabel);
        add(insuranceLabel);
        add(accountTypeLabel);
    }

    public void updateUserInfo(UserProfile user) {
        userNameLabel.setText("User: " + user.getName());
        insuranceLabel.setText("Insurance: " + user.getInsurance());
        accountTypeLabel.setText("Account Type: " + user.getAccountType());
    }

}
