package linfo2252.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import linfo2252.observer.Observable;
import linfo2252.observer.Observer;

/**
 * Encapsulates the configuration state of the User Interface.
 * <p>
 * This class serves as a <b>Presentation Model</b>. It stores purely visual properties 
 * (such as window titles, button visibility, and background colors) and implements 
 * {@link Observable} to allow the View components to react immediately when these 
 * properties change.
 */
public class UiState implements Observable {
    
    // Default visual properties
    private String title = "Application";
    private boolean buttonVisible = false;
    private Color background = Color.WHITE;

    private final List<Observer> observers = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Mutators (Trigger Updates)
    // -------------------------------------------------------------------------

    /**
     * Updates the main window title and notifies observers.
     */
    public void setTitle(String title) {
        this.title = title;
        notifyObservers();
    }

    /**
     * Toggles the visibility of the primary action button and notifies observers.
     */
    public void setButtonVisible(boolean visible) {
        this.buttonVisible = visible;
        notifyObservers();
    }

    /**
     * Updates the background color theme and notifies observers.
     */
    public void setBackground(Color color) {
        this.background = color;
        notifyObservers();
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getTitle() { 
        return title; 
    }
    
    public boolean isButtonVisible() { 
        return buttonVisible; 
    }
    
    public Color getBackground() { 
        return background; 
    }

    // -------------------------------------------------------------------------
    // Observable Implementation
    // -------------------------------------------------------------------------

    @Override 
    public void addObserver(Observer o) { 
        observers.add(o); 
    }
    
    @Override 
    public void removeObserver(Observer o) { 
        observers.remove(o); 
    }
    
    @Override 
    public void notifyObservers() { 
        observers.forEach(Observer::update); 
    }
}