package linfo2252.model;

import java.util.*;

import linfo2252.observer.Observable;
import linfo2252.observer.Observer;

public class FeatureManager implements Observable {   
    private final Map<String, Feature> features = new HashMap<>();
    private final List<Observer> observers = new ArrayList<>();
    
    public void registerFeature(Feature feature) {
        features.put(feature.getName(), feature);
    }
    
    public Feature getFeature(String name) {
        return features.get(name);
    }

    public boolean isFeatureActive(String name) {
        Feature f = features.get(name);
        return f != null && f.isActive();
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

    public void activate(String name) {
        Feature f = features.get(name);
        if (f != null && !f.isActive()) {
            f.activate();
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