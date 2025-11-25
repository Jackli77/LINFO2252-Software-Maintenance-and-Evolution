
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
    
    public List<String> getActiveFeatures() {
        List<String> active = new ArrayList<>();
        for (Feature f : features.values()) {
            if (f.isActive()) {
                active.add(f.getName());
            }
        }
        return active;
    }
    
    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }
    
    public void logSystemState(String action, String currentPage) {
        String activeStr = String.join(", ", getActiveFeatures());

        String logLine = String.format(
            "%s | action=%s | page=%s | activeFeatures=[%s]",
            java.time.LocalDateTime.now(),
            action,
            currentPage,
            activeStr
        );

        logs.add(logLine);
        notifyObservers(); // ConsoleView will refresh automatically
    }
    
    public void activate(String name) {
        Feature f = features.get(name);
        if (f != null && !f.isActive()) {
            f.activate();
            logSystemState("activate(" + name + ")", /* currentPage */ "UNKNOWN");
        }
    }

    public void deactivate(String name) {
        Feature f = features.get(name);
        if (f != null && f.isActive()) {
            f.deactivate();
            logSystemState("deactivate(" + name + ")", /* currentPage */ "UNKNOWN");
        }
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
