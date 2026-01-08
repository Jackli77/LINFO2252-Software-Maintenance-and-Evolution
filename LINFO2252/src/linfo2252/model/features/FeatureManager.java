package linfo2252.model.features;

import java.util.*;
import linfo2252.observer.Observable;
import linfo2252.observer.Observer;

/**
 * Manages the lifecycle of features.
 * Now purely a Data/Logic manager. Logging is delegated to the Controller.
 */
public class FeatureManager implements Observable {
    
    private final Map<String, Feature> features = new HashMap<>();
    private final List<Observer> observers = new ArrayList<>();
    
    public void registerFeature(Feature feature) {
        features.put(feature.getName(), feature);
    }
    
    // Needed by Controller to validate feature names
    public Feature getFeature(String name) {
        return features.get(name);
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
    
    public Set<String> getAvailableFeatures() {
        return features.keySet();
    }

    // --- State Changes ---

    public void activate(String name) {
        Feature f = features.get(name);
        if (f != null && !f.isActive()) {
            f.activate();
            // We notify observers that the model has changed
            notifyObservers(); 
        }
    }

    public void deactivate(String name) {
        Feature f = features.get(name);
        if (f != null && f.isActive()) {
            f.deactivate();
            notifyObservers();
        }
    }
    
    // --- Observer Pattern Implementation ---

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}