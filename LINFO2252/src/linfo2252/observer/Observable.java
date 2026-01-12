package linfo2252.observer;

/**
 *
 * @author celia
 */
public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
