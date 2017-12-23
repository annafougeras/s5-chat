/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.ICtrlVue;
import java.util.Observer;

/**
 *
 * @author Vincent Fougeras
 */
public abstract class BaseScreen extends javax.swing.JFrame implements Observer {
    
    protected ICtrlVue ctrlVue;
    
    public BaseScreen(ICtrlVue ctrlVue){
        this.ctrlVue = ctrlVue;
    }
    
}
