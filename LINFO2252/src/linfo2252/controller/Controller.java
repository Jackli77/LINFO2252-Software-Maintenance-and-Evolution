
package linfo2252.controller;

import java.util.Scanner;

import linfo2252.model.Model;
import linfo2252.view.ConsoleView;

/**
 *
 * @author celia
 */
public class Controller implements ControllerInterface {
    private final Model model;
    private final ConsoleView view;
    private boolean uiEnabled = true;
    
    public Controller() {
        this.model = new Model();
        this.view = new ConsoleView(model.getFeatureManager());
    }
    
    @Override
    public int activate(String[] deactivations, String[] activations ){
        //Desactivar primero
        if( deactivations != null ){
            for( String name : deactivations ){
                if( !model.getFeatureManager().getAvailableFeatures().contains(name)){
                    System.out.println( "Feature not found: " + name );
                    return 1;
                }
                
                model.getFeatureManager().deactivate(name);
            }
        }
        
        //Activar despues
        if( activations != null ){
            for( String name : activations ){
                if( !model.getFeatureManager().getAvailableFeatures().contains(name)){
                    System.out.println( "Feature not found: " + name );
                    return 1;
                }
                
                model.getFeatureManager().activate(name);
            }
        }
        
        return 0; //exito
    }
    
    @Override
    public boolean enableUIView(){
        if(uiEnabled){
            System.out.println( "UI already enabled." );
            return true;
        }
        
        uiEnabled = true;
        
        System.out.println( "UI enabled." );
        return true;
    }
    
    @Override
    public boolean disableUIView(){
        if( !uiEnabled ){
            System.out.println( "UI already disabled." );
            return true;
        }
        
        uiEnabled = false;
        
        System.out.println( "UI disabled (non-blocking mode)." );
        return true;
    }
    
    @Override
    public String[] getStateAsLog(){
        return model.getFeatureManager().getLogs().toArray(new String[0]);
    }
    
    public void start(){
        Scanner sc = new Scanner(System.in);
        System.out.println( "Smart Medical Appointment Manager (Console Version)" );
        System.out.println( "Commands: activate f1,f2 | deactivate f3 | show logs | show features | exit" );
        
        
        while( true ){
            System.out.println( "> " );
            String input = sc.nextLine().trim();
            if( input.equalsIgnoreCase( "exit" ) )break;
            
            if( input.equalsIgnoreCase( "show logs" ) ){
                view.showLogs();
            }
            else if( input.equalsIgnoreCase( "show features" ) ){
                System.out.println( "Available features: " + model.getFeatureManager().getAvailableFeatures() );
            }
            else if( input.startsWith("activate") || input.startsWith("deactivate")){
                
                String[] activations = null;
                String[] deactivations = null;
                
                
                if( input.startsWith( "activate" ) ){
                    activations = input.replace("activate", "").trim().split(",");
                }
                else if( input.startsWith( "deactivate" ) ){
                    deactivations = input.replace("deactivate", "").trim().split(",");
                }
                
                activate(deactivations,activations);
            }
            else{
                System.out.println("Unknown command!");
            }
        }
        sc.close();
    }
    
    public static void main( String[] args ){
        Controller c = new Controller();
        c.start();
    }
}
