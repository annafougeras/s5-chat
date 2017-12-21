/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import controleur.CtrlAdmin;
import controleur.ICtrlAdmin;
import vue.ConnectionScreen;

/**
 *
 * @author Vincent Fougeras
 */
public class AdminApp {   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the look and feel */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConnectionScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Create the default controller
        ICtrlAdmin ctrlAdmin = new CtrlAdmin();
    }   
    
}
