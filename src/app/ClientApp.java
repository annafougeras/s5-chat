/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import vue.ConnectionScreen;

import communication.ComAdresse;
import communication.simple.SimpleAdresse;

import controleur.CtrlVue;
import controleur.ICtrlVue;

/**
 *
 * @author Vincent Fougeras
 */
public class ClientApp { 
	
	// Adresse par défaut du serveur
	public static ComAdresse ADRESSE_SERVEUR = new SimpleAdresse("localhost", 8888);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    	
    	// Adresse du serveur précisée en ligne de commande
    	if (args.length == 2)
   			ADRESSE_SERVEUR = new SimpleAdresse(args[0], Integer.parseInt(args[1]));
    	
    	System.out.println("Serveur distant : " + ADRESSE_SERVEUR);
    	
        /* Set the look and feel */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConnectionScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Create the default controller
        ICtrlVue ctrlVue = new CtrlVue();
    }   
    
}
