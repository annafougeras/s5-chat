/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import modele.Groupe;
import modele.Message;
import modele.Ticket;
import vue.BaseScreen;
import vue.ConnectionScreen;
import vue.MainScreen;
import app.ClientApp;

import commChatS5.CtrlComClient;
import commChatS5.ICtrlComClient;
import commChatS5.Identifiants;

/**
 *
 * @author Vincent Fougeras
 */
public class CtrlVue extends Observable implements ICtrlVue {
	
	
	public enum Notification {
		UPDATE_JTREE;
	}
	
	

    ICtrlComClient ctrlComClient;
    BaseScreen currentScreen;
    NavigableSet<Groupe> model; /* Liste des groupes liés à l'utilisateur, qui contiennent des tickets, qui contiennent des messages ... */
    NavigableSet<Groupe> groupes; /* Liste de tous les groupes existants */
    
    /*
    @Override
    public void addObserver(Observer o){
        super.addObserver(o);
        System.out.println("Observer ajouté : " +  o);
        System.out.println("nombre d'observers : " + this.countObservers());
    }
    
    
    @Override
    public void deleteObserver(Observer o){
        super.deleteObserver(o);
        System.out.println("Observer supprimé : " +  o);
        System.out.println("nombre d'observers : " + this.countObservers());
    }
    */
    
    public CtrlVue(/*ComAdresse serverAddr*/){
        // Crée le ctrlCom
        ctrlComClient = new CtrlComClient(this, ClientApp.ADRESSE_SERVEUR);
        
        // Crée le modèle
        this.model = new TreeSet<>(new Comparator<Groupe>() {
            @Override
            public int compare(Groupe g1, Groupe g2) {
                return g1.getNom().compareTo(g2.getNom());
            }
        });
        
        this.groupes = new TreeSet<>(new Comparator<Groupe>() {
            @Override
            public int compare(Groupe g1, Groupe g2) {
                return g1.getNom().compareTo(g2.getNom());
            }
        });
        
        
        // Crée la vue
        currentScreen = new ConnectionScreen(this);
        this.addObserver(currentScreen);
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentScreen.setVisible(true);
            }
        });
    }

    /* 
        Méthodes de ICtrlVue
    */
    
    @Override
    public NavigableSet<Groupe> getModel(){
    	
    	// Le modèle est constitué des groupes ayant au moins un ticket visible
    	if (groupes.size() == 0)
    		getRemoteGroupes();
    	
    	model = new TreeSet<>();
    	Iterator<Groupe> iter = groupes.iterator();
    	for (;iter.hasNext();){
    		Groupe g = iter.next();
    		if (g.getTicketsConnus().size() > 0)
    			model.add(g);
    	}
    	return model;
    }
    
    @Override
    public NavigableSet<Groupe> getGroupes(){
    	if (groupes.size() == 0)
    		getRemoteGroupes();
        return this.groupes;
    }
    
    @Override
    public void getRemoteTickets() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getRemoteMessages(Ticket ticket) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void getRemoteGroupes() {
    	// Demande maintenant : bloquant
    	groupes = new TreeSet<>();
    	groupes.addAll(ctrlComClient.demanderTousLesGroupesBloquant());
    }

    @Override
    public void addTicket(Groupe destination, String title, String content) {
    	ctrlComClient.creerTicket(destination, title, content);
    }

    @Override
    public void addMessage(Ticket ticket, String message) {
    	ctrlComClient.creerMessage(ticket, message);
    }

    @Override
    public boolean connecter(String idUser, String password) {
    	
    	System.out.println("Patientez...");

    	Identifiants identifiants = new Identifiants(idUser, password);
    	boolean connecte = false;
    	
    	connecte = ctrlComClient.etablirConnexionBloquant(identifiants);
    	
    	System.out.println("Connexion établie : " + connecte);
    	        
        if(connecte)
            this.changeScreen(new MainScreen(this));            
 
        return connecte;

    }


    @Override
    public void deconnecter() {
    	ctrlComClient.deconnecter();
    	groupes = new TreeSet<Groupe>();
    	model = new TreeSet<Groupe>();
        this.changeScreen(new ConnectionScreen(this));
    }
    
        
    private void changeScreen(final BaseScreen newScreen){ // Peut être ne faire que des panel dans un seul JFrame ?
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentScreen.setVisible(false);
                deleteObserver(currentScreen);
                
                // En séquence, sinon on risque de deleteObserver après changé currentScreen!
                currentScreen = newScreen;
                addObserver(currentScreen);
                currentScreen.setVisible(true);
            }
        }); 
        /*
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentScreen = newScreen;
                addObserver(currentScreen);
                currentScreen.setVisible(true);
            }
        });
        */
    }
    
    

    /* 
        Méthodes de S5Client
        A faire : mettre à jour le modèle (groupes), puis notifier la vue
    */
    @Override
    public void recevoir(Ticket ticketRecu) {
    	
    	System.out.println("Réception ticket : " + ticketRecu);
    	
    	// Le groupe parent est déjà connu
    	if (groupes.contains(ticketRecu.getParent())) {
    		Groupe grp = groupes.floor((Groupe) ticketRecu.getParent());
    		
    		// Le ticket existe déjà : on le supprime
    		if (grp.getTicketsConnus().contains(ticketRecu)) 
    			grp.getTicketsConnus().remove(ticketRecu);
    		
    		// On ajoute le nouveau ticket
    		grp.getTicketsConnus().add(ticketRecu);
    		System.out.println(grp.getTicketsConnus());
    	}
    	
    	// Le groupe est inconnu, on demande la liste des groupes
    	else {
    		System.err.println("Réception d'un ticket pour un groupe inconnu");
    		ctrlComClient.demanderTousLesGroupes();
    	}
    	
        // Notifier le jTree : le modèle est mis à jour mais le JTree le prend en 
    	// compte UNIQUEMENT si on n'a jamais déplié le volet correspondant au groupe
        
    	// Je n'ai pas su me servir de notifyObservers(), j'appelle directement update 
    	//notifyObservers(Notification.UPDATE_JTREE);
        currentScreen.update(this, Notification.UPDATE_JTREE);
    }

    @Override
    public void recevoir(Set<Groupe> listeDesGroupes) {
    	// Reçoit une nouvelle liste de groupes (à tout moment)
    	groupes = new TreeSet<>();
    	groupes.addAll(listeDesGroupes);

    	// Je n'ai pas su me servir de notifyObservers(), j'appelle directement update 
    	//notifyObservers(Notification.UPDATE_JTREE);
        currentScreen.update(this, Notification.UPDATE_JTREE);
    }

    @Override
    public void recevoir(boolean accuseConnexion) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void recevoir(Message messageRecu) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void recevoir(Object messageInconnu) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    

    
    
}
