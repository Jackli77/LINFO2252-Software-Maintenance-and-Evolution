
package linfo2252.model;

/**
 *
 * @author celia
 */
public class Model {
    private final FeatureManager featureManager = new FeatureManager();
    
    public Model(){
        featureManager.registerFeature(new UserFeature());
        featureManager.registerFeature(new AppointmentFeature());
    }
    
    public FeatureManager getFeatureManager(){
        return featureManager;
    }
}
