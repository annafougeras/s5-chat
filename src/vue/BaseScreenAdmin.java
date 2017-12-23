/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
