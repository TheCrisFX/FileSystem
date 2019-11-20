/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystemfs;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
/**
 *
 * @author cris9
 */
public class Editor implements NativeKeyListener{

    
    
    public String caracter;
    public String nueva;
    public Editor(){
		try {
                    // Clear previous logging configurations.
                        LogManager.getLogManager().reset();

                        // Get the logger for "org.jnativehook" and set the level to off.
                        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
                        logger.setLevel(Level.OFF);
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

                GlobalScreen.addNativeKeyListener(new Editor());
    }
    
    public String start() throws NativeHookException{
        while(!caracter.equals("Escape")){
        
        }
        GlobalScreen.unregisterNativeHook();
        return nueva;
    }
    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
		caracter = NativeKeyEvent.getKeyText(e.getKeyCode());
                nueva+=caracter;

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
