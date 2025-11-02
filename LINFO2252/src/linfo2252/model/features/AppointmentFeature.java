
package linfo2252.model.features;

/**
 *
 * @author celia
 */
public class AppointmentFeature extends Feature{
    public AppointmentFeature(){
        super("feature_appointments");
    }
    
    @Override
    protected void onActivate(){
        System.out.println("Appointment feature activated: add appointment scheduling logic");
    }
    
    @Override
    protected void onDeactivate(){
        System.out.println("Appointment feature deactivated: remove scheduling logic");
    }
}
