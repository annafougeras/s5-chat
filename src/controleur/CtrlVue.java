/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import commChatS5.ICtrlComClient;
import java.util.Comparator;
import java.util.Date;
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

/**
 *
 * @author Vincent Fougeras
 */
public class CtrlVue extends Observable implements ICtrlVue {

    ICtrlComClient ctrlComClient;
    BaseScreen currentScreen;
    NavigableSet<Groupe> groupes;
    
    public CtrlVue(/*ComAdresse serverAddr*/){
        // Crée le ctrlCom
        /*ctrlComClient = new CtrlComClient(this, serverAddr);*/
        
        // Crée le modèle
        this.groupes = new TreeSet<>(new Comparator<Groupe>() {
            @Override
            public int compare(Groupe g1, Groupe g2) {
                return g1.getNom().compareTo(g2.getNom());
            }
        });
        
        NavigableSet<Ticket> tickets = new TreeSet<Ticket>();
        tickets.add(new Ticket(222, "Un ticket", 3, new Date()));
        groupes.add(new Groupe(111, "Info 3A", tickets));
        
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
        return this.groupes;
    }
    
    @Override
    public void getTickets() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getMessages(Ticket ticket) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void getGroupes() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        boolean connecte = true;
        
        if(connecte){
            this.changeScreen(new MainScreen(this));            
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public void deconnecter() {
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
        throw new UnsupportedOperationException("Not supported yet.");
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
