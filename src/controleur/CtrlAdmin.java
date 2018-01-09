/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import modele.Groupe;
import modele.Identifiable;
import modele.KeyIdentifiable;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;
import vue.AdminScreen;
import vue.BaseScreenAdmin;

import commChatS5.CtrlComAdmin;
import commChatS5.ICtrlComAdmin;
import commChatS5.Identifiants;
import communication.ComAdresse;
import communication.simple.SimpleAdresse;


public class CtrlAdmin extends Observable implements ICtrlAdmin {
	
	public static final ComAdresse SERVEUR_LOCAL = new SimpleAdresse("localhost", 8888);
    
	private NavigableSet<Groupe> modele;
	private Map<KeyIdentifiable,Groupe> groupeParId;
    private NavigableMap<Groupe, NavigableSet<Utilisateur>> mapGroupeUser;
    private NavigableMap<Utilisateur, NavigableSet<Groupe>> mapUserGroupe;
    BaseScreenAdmin currentScreen;
    private ICtrlComAdmin ctrlComAdmin;
    
     public CtrlAdmin(ComAdresse serverAddr){
        // Crée le ctrlCom
        ctrlComAdmin = new CtrlComAdmin(this, serverAddr);
        
        // Crée le modèle
        modele = new TreeSet<>();
        mapGroupeUser = new TreeMap<>();
        mapUserGroupe = new TreeMap<>();
           
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
 	public boolean connecter(String passAdmin) {
 		boolean cnx = ctrlComAdmin.etablirConnexionBloquant(new Identifiants("admin", passAdmin));
 		if (cnx) {
	 		ctrlComAdmin.demanderGroupes();
	 		ctrlComAdmin.demanderUtilisateursParGroupe();
 		}
 		return cnx;
 	}


 	@Override
 	public void deconnecter() {
 		ctrlComAdmin.deconnecter();
 	}


     
     
     
     
 	private void updateModele(Set<Groupe> groupes){
 		modele = new TreeSet<>();
 		groupeParId = new HashMap<>();
 		for (Groupe g: groupes) {
 			modele.add(g);
 			groupeParId.put(new KeyIdentifiable(g), g);
 		}
 	}
     
     
    private void updateMaps(Map<Groupe,NavigableSet<Utilisateur>> map){
    	mapGroupeUser = new TreeMap<>();
    	mapUserGroupe = new TreeMap<>();
    	mapGroupeUser.putAll(map);
    	for (Map.Entry<Groupe, NavigableSet<Utilisateur>> e: map.entrySet()) {
    		for (Utilisateur u: e.getValue()) {
    			if (!mapUserGroupe.containsKey(u))
    				mapUserGroupe.put(u, new TreeSet<Groupe>());
    			mapUserGroupe.get(u).add(e.getKey());
    		}
    	}
    }
    

    @Override
    public NavigableSet<Groupe> getGroupes() {
    	return modele;
    }

    @Override
    public NavigableSet<Groupe> getGroupes(Utilisateur utilisateur) {
    	if (mapUserGroupe.containsKey(utilisateur))
    		return mapUserGroupe.get(utilisateur);
    	return new TreeSet<Groupe>();
    }

    @Override
    public NavigableSet<Utilisateur> getUtilisateurs() {
    	return new TreeSet<Utilisateur>(mapUserGroupe.keySet());
    	
    }

    @Override
    public NavigableSet<Utilisateur> getUtilisateurs(Groupe groupe) {
    	if (mapGroupeUser.containsKey(groupe))
    		return mapGroupeUser.get(groupe);
    	return new TreeSet<Utilisateur>();
    }

    @Override
    public void getRemoteGroupes() {
    	ctrlComAdmin.demanderGroupes();
    	ctrlComAdmin.demanderUtilisateursParGroupe();
    }

    @Override
    public void getRemoteUtilisateurs() {
    	ctrlComAdmin.demanderUtilisateurs();
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
    	ctrlComAdmin.insererUtilisateur(utilisateur);
    }

    @Override
    public void insererGroupe(Groupe groupe) {
    	ctrlComAdmin.insererGroupe(groupe);
    }

    @Override
    public void supprimerUtilisateur(Utilisateur utilisateur) {
    	ctrlComAdmin.supprimerUtilisateur(utilisateur);
    }

    @Override
    public void supprimerGroupe(Groupe groupe) {
    	ctrlComAdmin.supprimerGroupe(groupe);
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
    	updateModele(tousLesGroupes);
		currentScreen.update(this, null);
    	
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
    	Identifiable idGroupe = ticket.getParent();
    	Groupe groupe;
    	if (groupeParId.containsKey(idGroupe)) {
    		groupe = groupeParId.get(idGroupe);
    		groupe.getTicketsConnus().remove(new KeyIdentifiable(ticket));
    		groupe.addTicketConnu(ticket);
    		currentScreen.update(this, null);
    	}
    	else
    		ctrlComAdmin.demanderGroupes();
    }

    @Override
    public void recevoirTicket(Set<Ticket> tousLesTickets) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recevoirGroupe(Groupe groupe) {
    	System.out.println("Groupe reçu : " + groupe);
    	modele.add(groupe);
    	currentScreen.update(this, null);
    }
    
	@Override
	public void recevoirUtilisateurParGroupe(Map<Groupe, NavigableSet<Utilisateur>> map) {
		updateMaps(map);
		currentScreen.update(this, null);
	}
    
    @Override
    @Deprecated
    public void recevoirReponseSQL(String reponse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void recevoirMessageInvalide(Object messageInvalide) {
    	//System.err.println("Message invalide reçu: " + messageInvalide);
    }



    
}
