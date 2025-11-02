
package linfo2252.view;

import linfo2252.model.features.FeatureManager;
import linfo2252.observer.Observer;

import java.util.List;


/**
 *
 * @author celia
 */
public class ConsoleView implements Observer{
    private final FeatureManager manager;
    
    public ConsoleView( FeatureManager manager ){
        this.manager = manager;
        manager.addObserver(this);
    }
    
    public void showLogs(){
        System.out.println("\n===== SYSTEM LOGS =====");
        for( String log : manager.getLogs() ){
            System.out.println( log );
        }
        System.out.println("========================\n");
    }
    
    @Override
    public void update(){
        showLogs();
    }
}
