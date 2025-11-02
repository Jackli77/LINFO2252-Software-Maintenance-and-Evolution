
package linfo2252.model.features;


import java.util.*;


import linfo2252.observer.Observable;
import linfo2252.observer.Observer;

/**
 *
 * @author celia
 */
public class FeatureManager implements Observable{
    private final Map<String, Feature> features = new HashMap<>();
    private final List<String> logs = new ArrayList<>();
    
    private final List<Observer> observers = new ArrayList<>();
    
    
    public void registerFeature( Feature feature ){
        features.put(feature.getName(), feature);
    }   
    
    public void activate( String... names ){
        for( String name : names ){
            Feature f = features.get(name.trim());
            if( f != null ){
                f.activate();
                logs.add( "[Activated]" + name );
            }
            else{
                logs.add("[Error] Feature not found: " + name );
            }
        }
        
        notifyObservers();
    }
    
    public void deactivate( String... names ){
        for( String name : names ){
            Feature f = features.get(name.trim());
            if( f != null ){
                f.deactivate();
                logs.add( "[Deactivated]" + name );
            }
            else{
                logs.add("[Error] Feature not found: " + name );
            }
        }
        
        notifyObservers();
    }
    
    public List<String> getLogs(){
        return logs;
    }
    
    public Set<String> getAvailableFeatures(){
        return features.keySet();
    }
    
    @Override
    public void addObserver(Observer observer){
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(){
        for( Observer o : observers ){
            o.update();
        }
    }
}
