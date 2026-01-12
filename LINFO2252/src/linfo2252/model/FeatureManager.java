package linfo2252.model;

import java.util.*;
import linfo2252.observer.Observable;
import linfo2252.observer.Observer;

/**
 * Manages the lifecycle and state of all system features.
 * <p>
 * This class serves as the central registry for {@link Feature} objects.
 * It implements the {@link Observable} interface to notify the UI (Views) whenever
 * a feature is activated or deactivated, enabling real-time interface adaptability.
 */
public class FeatureManager implements Observable {
    
    // Registry mapping unique feature names to their instances
    private final Map<String, Feature> features = new HashMap<>();
    
    // List of subscribers (observers) monitoring feature state changes
    private final List<Observer> observers = new ArrayList<>();
    
    /**
     * Registers a new feature with the manager.
     *
     * @param feature The initialized feature to be managed.
     */
    public void registerFeature(Feature feature) {
        features.put(feature.getName(), feature);
    }
    
    /**
     * Retrieves a registered feature by its name.
     *
     * @param name The unique identifier of the feature.
     * @return The Feature object, or null if not found.
     */
    public Feature getFeature(String name) {
        return features.get(name);
    }

    /**
     * Convenience method to check if a feature is currently enabled.
     * Safe to call even if the feature name is invalid (returns false).
     *
     * @param name The unique identifier of the feature.
     * @return true if the feature exists and is active; false otherwise.
     */
    public boolean isFeatureActive(String name) {
        Feature f = features.get(name);
        return f != null && f.isActive();
    }
    
    /**
     * Returns a list of the names of all currently active features.
     */
    public List<String> getActiveFeatures() {
        List<String> active = new ArrayList<>();
        for (Feature f : features.values()) {
            if (f.isActive()) {
                active.add(f.getName());
            }
        }
        return active;
    }
    
    /**
     * Returns the set of all registered feature names.
     */
    public Set<String> getAvailableFeatures() {
        return features.keySet();
    }

    // -------------------------------------------------------------------------
    // State Management & Notification
    // -------------------------------------------------------------------------

    /**
     * Activates the specified feature and notifies all observers.
     * If the feature is already active or does not exist, no action is taken.
     *
     * @param name The name of the feature to activate.
     */
    public void activate(String name) {
        Feature f = features.get(name);
        if (f != null && !f.isActive()) {
            f.activate();
            // Notify Views (e.g., MainView, ConsoleView) to refresh their state
            notifyObservers(); 
        }
    }

    /**
     * Deactivates the specified feature and notifies all observers.
     * If the feature is already inactive or does not exist, no action is taken.
     *
     * @param name The name of the feature to deactivate.
     */
    public void deactivate(String name) {
        Feature f = features.get(name);
        if (f != null && f.isActive()) {
            f.deactivate();
            notifyObservers();
        }
    }    

    // -------------------------------------------------------------------------
    // Observable Implementation
    // -------------------------------------------------------------------------

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