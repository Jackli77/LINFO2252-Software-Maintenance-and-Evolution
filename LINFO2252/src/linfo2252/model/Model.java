package linfo2252.model;

import linfo2252.logger.Logger;

public class Model {
    private Logger logger;
    private final FeatureManager featureManager = new FeatureManager();
    private final TimeEventSystem tes = new TimeEventSystem(this);

    public Model(Logger logger){
        this.logger = logger;
        featureManager.registerFeature(new UserFeature());
        featureManager.registerFeature(new AppointmentFeature());
        featureManager.registerFeature(new InsuranceFeature());
        featureManager.registerFeature(new MedHistoryFeature());
    }

    public FeatureManager getFeatureManager(){
        return featureManager;
    }

    public TimeEventSystem getTES() {
        return tes;
    }

    public Logger getLogger() { 
        return logger; 
    }
}
