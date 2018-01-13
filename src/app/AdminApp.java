package app;

import vue.ConnectionScreen;

import communication.ComAdresse;
import communication.simple.SimpleAdresse;

import controleur.CtrlAdmin;
import controleur.ICtrlAdmin;

/**
 *
 * @author Vincent Fougeras
 */
public class AdminApp {   
	
	
	// Adresse par défaut du serveur
	public static ComAdresse ADRESSE_SERVEUR = new SimpleAdresse("localhost", 8888);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    	
    	// Adresse du serveur précisée en ligne de commande
    	if (args.length == 2)
   			ADRESSE_SERVEUR = new SimpleAdresse(args[0], Integer.parseInt(args[1]));
    	
   	
        /* Set the look and feel */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConnectionScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Create the default controller
        String passAdmin = "admin";
        ICtrlAdmin ctrlAdmin = new CtrlAdmin(ADRESSE_SERVEUR);
        
        
        // Le temps que le serveur démarre...
        try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Connexion automatique : " + ctrlAdmin.connecter(passAdmin));
    }   
    
}
