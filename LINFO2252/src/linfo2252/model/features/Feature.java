
package linfo2252.model.features;

/**
 *
 * @author celia
 */
public abstract class Feature {
    private final String name;
    private boolean active;
    
    public Feature( String name ){
        this.name = name;
        this.active = false;
    }
    
    public String getName(){
        return name;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public void activate(){
        if( !active ){
            active = true;
            onActivate();
        }
    }
    
    public void deactivate(){
        if( active ){
            active = false;
            onDeactivate();
        }
    }
    //Definir en subclases
    protected abstract void onActivate();
    protected abstract void onDeactivate();
    
}
