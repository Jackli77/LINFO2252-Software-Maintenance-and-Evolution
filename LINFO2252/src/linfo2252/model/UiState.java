package linfo2252.model;

import java.awt.Color;
import linfo2252.observer.Observable;
import linfo2252.observer.Observer;
import java.util.ArrayList;
import java.util.List;

public class UiState implements Observable {
    private String title = "Application";
    private boolean buttonVisible = false;
    private Color background = Color.WHITE;

    private final List<Observer> observers = new ArrayList<>();

    public void setTitle(String title){
        this.title = title;
        notifyObservers();
    }

    public void setButtonVisible(boolean visible){
        this.buttonVisible = visible;
        notifyObservers();
    }

    public void setBackground(Color color){
        this.background = color;
        notifyObservers();
    }

    public String getTitle(){ return title; }
    public boolean isButtonVisible(){ return buttonVisible; }
    public Color getBackground(){ return background; }

    @Override public void addObserver(Observer o){ observers.add(o); }
    @Override public void removeObserver(Observer o){ observers.remove(o); }
    @Override public void notifyObservers(){ observers.forEach(Observer::update); }
}
