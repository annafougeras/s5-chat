package vue;

import controleur.ICtrlClient;
import java.util.Observer;

/**
 *
 * @author Vincent Fougeras
 */
public abstract class BaseScreenClient extends javax.swing.JFrame implements Observer {
    
    protected ICtrlClient ctrlClient;
    
    public BaseScreenClient(ICtrlClient ctrlClient){
        this.ctrlClient = ctrlClient;
    }
    
}
