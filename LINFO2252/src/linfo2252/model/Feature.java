package linfo2252.model;

public class Feature { 
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
    
    public void activate() {
        if (!active) {
            active = true;
        }
    }
    
    public void deactivate() {
        if (active) {
            active = false;
        }
    }
}