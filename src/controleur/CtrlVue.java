/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.util.Comparator;
import java.util.Date;
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
    public void addTicket(Groupe destination, String content, String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addMessage(Ticket ticket, String message) {
        throw new UnsupportedOperationException("Not supported yet.");
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
            }
        }); 
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentScreen = newScreen;
                addObserver(currentScreen);
                currentScreen.setVisible(true);
            }
        });
    }
    
    

    /* 
        Méthodes de S5Client
        A faire : mettre à jour le modèle (groupes), puis notifier la vue
    */
    @Override
    public void recevoir(Ticket ticketRecu) {
        throw new UnsupportedOperationException("Not supported yet.");
        
        //this.notifyObservers();
    }

    @Override
    public void recevoir(Set<Groupe> listeDesGroupes) {
    	// Reçoit une nouvelle liste de groupes (à tout moment)
    	groupes = new TreeSet<>();
    	groupes.addAll(listeDesGroupes);
    	//TODO this.updateScreen() !
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
