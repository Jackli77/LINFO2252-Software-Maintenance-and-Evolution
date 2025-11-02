package linfo2252.model;

import java.util.ArrayList;

import linfo2252.model.features.AppointmentFeature;
import linfo2252.model.features.FeatureManager;
import linfo2252.model.features.InsuranceFeature;
import linfo2252.model.features.MedHistoryFeature;
import linfo2252.model.features.UserFeature;

public class Model {
	private final java.util.List<Appointment> appointments = new ArrayList<>();
    private final FeatureManager featureManager = new FeatureManager();
    private final TimeEventSystem tes = new TimeEventSystem(this);

    public Model(){
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
}
