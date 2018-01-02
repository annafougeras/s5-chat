/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import modele.Groupe;
import modele.Identifiable;
import modele.KeyIdentifiable;
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
		UPDATE_JTREE,
		UPDATE_MESSAGES;
	}
	
	public class RunnableNotification implements Runnable {

		private Observable emetteur;
		private Notification notification;
		
		public RunnableNotification(Observable emetteur, Notification notification){
			this.emetteur = emetteur;
			this.notification = notification;
		}
		
		@Override
		public void run() {
			currentScreen.update(emetteur, notification);
		}
	}
	
	

    ICtrlComClient ctrlComClient;
    BaseScreen currentScreen;
    NavigableSet<Groupe> model; /* Liste des groupes liés à l'utilisateur, qui contiennent des tickets, qui contiennent des messages ... */
    NavigableSet<Groupe> groupes; /* Liste de tous les groupes existants */
    
    Map<KeyIdentifiable,Groupe> groupesParId;
    Map<KeyIdentifiable,Ticket> ticketsParId;
    
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
    
    
    
    private void majGroupes(Set<Groupe> nouveauxGroupes){
    	groupes = new TreeSet<>();
    	groupesParId = new HashMap<>();
    	ticketsParId = new HashMap<>();
    	groupes.addAll(nouveauxGroupes);
    	for (Groupe g: groupes){
    		groupesParId.put(new KeyIdentifiable(g.getIdentifiantUnique()), g);
    		for (Ticket t: g.getTicketsConnus())
    			ticketsParId.put(new KeyIdentifiable(t.getIdentifiantUnique()), t);
    	}
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
    	majGroupes(ctrlComClient.demanderTousLesGroupesBloquant());
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
                
                // En séquence, sinon on risque de deleteObserver après changé currentScreen ?
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
    	
    	System.out.println("Ticket reçu : " + ticketRecu);
    	
    	Identifiable groupeParent = new KeyIdentifiable(ticketRecu.getParent());
    	Identifiable ticket = new KeyIdentifiable(ticketRecu);
    	
    	// Le groupe parent est déjà connu
    	if (groupesParId.containsKey(groupeParent)) {
    		
    		Groupe grp = groupesParId.get(groupeParent);
    		
    		// Le ticket existe déjà : on le supprime
    		if (ticketsParId.containsKey(ticket)) {
    			grp.getTicketsConnus().remove(ticketsParId.get(ticket));
    			ticketsParId.remove(ticket);
    		}
    		
    		// On ajoute le nouveau ticket
    		grp.getTicketsConnus().add(ticketRecu);
    		ticketsParId.put(new KeyIdentifiable(ticketRecu), ticketRecu);
    		
        	// Je n'ai pas su me servir de notifyObservers(), j'appelle directement update 
        	//notifyObservers(Notification.UPDATE_JTREE);
            java.awt.EventQueue.invokeLater(new RunnableNotification(this, Notification.UPDATE_JTREE));
    	}
    	
    	// Le groupe est inconnu, on demande la liste des groupes
    	else {
    		System.out.println("Groupe inconnu -> demandé");
    		ctrlComClient.demanderTousLesGroupes();
    	}
        
    }

    @Override
    public void recevoir(Set<Groupe> listeDesGroupes) {
    	// Reçoit une nouvelle liste de groupes (à tout moment)
    	System.out.println("Liste des groupes reçue");
    	majGroupes(listeDesGroupes);

    	// Je n'ai pas su me servir de notifyObservers(), j'appelle directement update 
    	//notifyObservers(Notification.UPDATE_JTREE);
        java.awt.EventQueue.invokeLater(new RunnableNotification(this, Notification.UPDATE_JTREE));

    }

    @Override
    public void recevoir(boolean accuseConnexion) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void recevoir(Message messageRecu) {
    	System.out.println("Message reçu : " + messageRecu);
    	Identifiable ticketParent = messageRecu.getParent();
    	Identifiable groupeParent = ticketParent.getParent();
    	
    	if (groupesParId.containsKey(groupeParent)){
    		
    		if (ticketsParId.containsKey(ticketParent)){
    			
    			Ticket t = ticketsParId.get(ticketParent);
    			t.addMessage(messageRecu);
    			
    	    	// Je n'ai pas su me servir de notifyObservers(), j'appelle directement update 
    	    	//notifyObservers(Notification.UPDATE_JTREE);
    	        java.awt.EventQueue.invokeLater(new RunnableNotification(this, Notification.UPDATE_MESSAGES));
    		}
    		else {
    			System.out.println("Ticket inconnu -> demandé");
    			ctrlComClient.demanderTicket(ticketParent);
    		}
    	}
    	else {
    		System.out.println("Groupe inconnu -> demandé");
    		System.out.println(groupesParId);
    		ctrlComClient.demanderTousLesGroupes();
    	}
    	
    }

    @Override
    public void recevoir(Object messageInconnu) {
    	//TODO Faire plus ?
    	System.err.println("Message inconnu reçu : " + messageInconnu);
    }

    
    

    
    
}
