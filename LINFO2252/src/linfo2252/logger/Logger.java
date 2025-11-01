package linfo2252.logger;

public interface Logger {
    void log(String message);
    void updateUI(Runnable uiAction);
}

