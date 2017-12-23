/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.util.Comparator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;
import modele.Groupe;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;
import vue.AdminScreen;
import vue.BaseScreenAdmin;


public class CtrlAdmin extends Observable implements ICtrlAdmin {
    
    NavigableMap<Groupe, Utilisateur> model;
    BaseScreenAdmin currentScreen;
    
     public CtrlAdmin(/*ComAdresse serverAddr*/){
        // Crée le ctrlCom
        /*ctrlComAdmin = new CtrlComAdmin(this, serverAddr);*/
        
        // Crée le modèle
        this.model = new TreeMap<>(new Comparator<Groupe>() {
            @Override
            public int compare(Groupe g1, Groupe g2) {
                return g1.getNom().compareTo(g2.getNom());
            }
        });
        
           
        // Crée la vue
        currentScreen = new AdminScreen(this);
        this.addObserver(currentScreen);
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentScreen.setVisible(true);
            }
        });
    }
    
    

    @Override
    public NavigableSet<Groupe> getGroupes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NavigableSet<Groupe> getGroupes(Utilisateur utilisateur) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NavigableSet<Utilisateur> getUtilisateurs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NavigableSet<Utilisateur> getUtilisateurs(Groupe groupe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getRemoteGroupes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getRemoteUtilisateurs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeUtilisateurFromGroupe(Utilisateur utilisateur, Groupe groupe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addUtilisateurToGroupe(Utilisateur utilisateur, Groupe groupe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insererUtilisateur(Utilisateur utilisateur) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insererGroupe(Groupe groupe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void supprimerUtilisateur(Utilisateur utilisateur) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void supprimerGroupe(Groupe groupe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    /*
        Méthodes de S5Admin
    */
    @Override
    public void recevoir(boolean accuseDeConnexion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   

    @Override
    public void recevoirUtilisateur(Set<Utilisateur> tousLesUtilisateurs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recevoirGroupe(Set<Groupe> tousLesGroupes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    
    
    
    
    @Override
    public void recevoirUtilisateur(Utilisateur utilisateur) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void recevoirMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recevoirMessage(Set<Message> tousLesMessages) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recevoirTicket(Ticket ticket) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recevoirTicket(Set<Ticket> tousLesTickets) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recevoirGroupe(Groupe groupe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void recevoirReponseSQL(String reponse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
