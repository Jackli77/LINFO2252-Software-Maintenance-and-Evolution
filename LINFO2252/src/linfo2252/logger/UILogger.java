package linfo2252.logger;
import linfo2252.view.UIView;

public class UILogger implements Logger {
    private final UIView view;

    public UILogger(UIView view){
        this.view = view;
    }

    @Override
    public void log(String message){
        view.appendToLog(message);
    }

    @Override
    public void updateUI(Runnable uiAction){
        uiAction.run();   // Always ensure this runs on Swing thread if needed
        view.refresh();
    }
}
