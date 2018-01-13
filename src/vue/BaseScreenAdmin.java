package vue;

import controleur.ICtrlAdmin;
import java.util.Observer;

/**
 *
 * @author Vincent Fougeras
 */
public abstract class BaseScreenAdmin extends javax.swing.JFrame implements Observer {
    
    protected ICtrlAdmin ctrlAdmin;
    
    public BaseScreenAdmin(ICtrlAdmin ctrlAdmin){
        this.ctrlAdmin = ctrlAdmin;
    }
    
}
