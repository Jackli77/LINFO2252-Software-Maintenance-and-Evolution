
package linfo2252.controller;

import linfo2252.model.*;
import linfo2252.view.ConsoleView;

/**
 *
 * @author celia
 */
public class TestMain {
    public static void main(String[] args){
        Model model = new Model();
        
        model.getFeatureManager().activate("feature_user");
        model.getFeatureManager().activate("feature_appointments");
        model.getFeatureManager().deactivate("feature_user");
    }
}
