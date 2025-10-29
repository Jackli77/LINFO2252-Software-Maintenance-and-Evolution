
package linfo2252.model;

/**
 *
 * @author celia
 */
public class UserFeature extends Feature {
    public UserFeature(){
        super("feature_user");
    }
    
    @Override
    protected void onActivate(){
        System.out.println("User feature activated: add buttons for login/profile");
    }
    
    @Override
    protected void onDeactivate(){
        System.out.println("User feature deactivated: remove login/profile UI");
    }
}
