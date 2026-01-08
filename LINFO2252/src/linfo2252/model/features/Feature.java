package linfo2252.model.features;

public abstract class Feature {
    private final String name;
    private boolean active;
    
    public Feature(String name) {
        this.name = name;
        this.active = false;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isActive() {
        return active;
    }
    
    // Package-private or public methods to change state
    // We only want the FeatureManager to call these usually
    public void activate() {
        if (!active) {
            active = true;
            onActivate();
        }
    }
    
    public void deactivate() {
        if (active) {
            active = false;
            onDeactivate();
        }
    }

    // Abstract methods for specific behavior (Strategy Pattern)
    protected abstract void onActivate();
    protected abstract void onDeactivate();
}