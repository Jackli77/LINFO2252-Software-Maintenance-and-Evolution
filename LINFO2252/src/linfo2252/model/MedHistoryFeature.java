
package linfo2252.model;

/**
 *
 * @author celia
 */
public class MedHistoryFeature extends Feature{
    public MedHistoryFeature(){
        super("feature_medhistory");
    }
    
    @Override
    protected void onActivate(){
        System.out.println("Medical history feature activated: add appointment scheduling logic");
    }
    
    @Override
    protected void onDeactivate(){
        System.out.println("Medical history feature deactivated: remove scheduling logic");
    }
}
