package linfo2252.model.features;

public class InsuranceFeature extends Feature{
    public InsuranceFeature(){
        super("feature_insurance");
    }
    
    @Override
    protected void onActivate(){
        System.out.println("Insurance feature activated: add appointment scheduling logic");
    }
    
    @Override
    protected void onDeactivate(){
        System.out.println("Insurance history feature deactivated: remove scheduling logic");
    }
}
