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
	private Map<KeyIdentifiable, Groupe> groupeParId;
	private NavigableMap<Groupe, NavigableSet<Utilisateur>> mapGroupeUser;
	private NavigableMap<Utilisateur, NavigableSet<Groupe>> mapUserGroupe;
	private NavigableSet<Utilisateur> utilisateurs;

	BaseScreenAdmin currentScreen;
	private ICtrlComAdmin ctrlComAdmin;

	/**
	 * 
	 * @param serverAddr
	 *            l'adresse du serveur
	 */
	public CtrlAdmin(ComAdresse serverAddr) {
		// Crée le ctrlCom
		ctrlComAdmin = new CtrlComAdmin(this, serverAddr);

		// Crée le modèle
		modele = new TreeSet<>();
		mapGroupeUser = new TreeMap<>();
		mapUserGroupe = new TreeMap<>();
		utilisateurs = new TreeSet<>();

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

	/*
	 * Méthodes de ICtrlAdmin
	 */

	@Override
	public boolean connecter(String passAdmin) {
		boolean isConnecte = ctrlComAdmin.etablirConnexionBloquant(new Identifiants("admin", passAdmin));
		if (isConnecte) {
			ctrlComAdmin.demanderGroupes();
			ctrlComAdmin.demanderUtilisateurs();
			ctrlComAdmin.demanderUtilisateursParGroupe();
		}
		return isConnecte;
	}

	@Override
	public void deconnecter() {
		ctrlComAdmin.deconnecter();
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
		return utilisateurs;

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
		ctrlComAdmin.quitterGroupe(new KeyIdentifiable(groupe), new KeyIdentifiable(utilisateur));
		ctrlComAdmin.demanderUtilisateursParGroupe();
	}

	@Override
	public void addUtilisateurToGroupe(Utilisateur utilisateur, Groupe groupe) {
		ctrlComAdmin.rejoindreGroupe(new KeyIdentifiable(groupe), new KeyIdentifiable(utilisateur));
		ctrlComAdmin.demanderUtilisateursParGroupe();
	}

	@Override
	public void insererUtilisateur(Utilisateur utilisateur) {
		ctrlComAdmin.insererUtilisateur(utilisateur);
		ctrlComAdmin.demanderUtilisateurs();
	}

	@Override
	public void insererUtilisateur(String nom, String prenom, int idUnique) {
		Utilisateur user = new Utilisateur(idUnique, nom, prenom);
		insererUtilisateur(user);
	}

	@Override
	public void insererGroupe(Groupe groupe) {
		ctrlComAdmin.insererGroupe(groupe);
		ctrlComAdmin.demanderGroupes();
		ctrlComAdmin.demanderUtilisateursParGroupe();
	}

	@Override
	public void insererGroupe(String nom) {
		Groupe groupe = new Groupe(0, nom);
		insererGroupe(groupe);
	}

	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		ctrlComAdmin.supprimerUtilisateur(utilisateur);
		ctrlComAdmin.demanderUtilisateurs();
		ctrlComAdmin.demanderUtilisateursParGroupe();
	}

	@Override
	public void supprimerGroupe(Groupe groupe) {
		ctrlComAdmin.supprimerGroupe(groupe);
		ctrlComAdmin.demanderGroupes();
	}

	/*
	 * Méthodes privées
	 */

	private void updateModele(Set<Groupe> groupes) {
		modele = new TreeSet<>();
		groupeParId = new HashMap<>();
		for (Groupe currGroupe : groupes) {
			modele.add(currGroupe);
			groupeParId.put(new KeyIdentifiable(currGroupe), currGroupe);
		}
	}

	private void updateMaps(Map<Groupe, NavigableSet<Utilisateur>> newMap) {
		mapGroupeUser = new TreeMap<>();
		mapUserGroupe = new TreeMap<>();
		mapGroupeUser.putAll(newMap);
		for (Map.Entry<Groupe, NavigableSet<Utilisateur>> currEntry : newMap.entrySet()) {
			for (Utilisateur currUser : currEntry.getValue()) {
				if (!mapUserGroupe.containsKey(currUser))
					mapUserGroupe.put(currUser, new TreeSet<Groupe>());
				mapUserGroupe.get(currUser).add(currEntry.getKey());
			}
		}
	}

	/*
	 * Méthodes de S5Admin
	 */

	@Override
	public void recevoirGroupe(Set<Groupe> tousLesGroupes) {
		updateModele(tousLesGroupes);

		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void recevoirUtilisateurs(NavigableSet<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;

		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void recevoirTicket(Ticket ticket) {
		Identifiable idGroupe = ticket.getParent();
		Groupe groupe;
		if (groupeParId.containsKey(idGroupe)) {
			groupe = groupeParId.get(idGroupe);
			groupe.getTicketsConnus().remove(new KeyIdentifiable(ticket));
			groupe.addTicketConnu(ticket);

			this.setChanged();
			this.notifyObservers();
		} else
			ctrlComAdmin.demanderGroupes();
	}

	@Override
	public void recevoirGroupe(Groupe groupe) {
		modele.add(groupe);

		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void recevoirUtilisateurParGroupe(Map<Groupe, NavigableSet<Utilisateur>> map) {
		updateMaps(map);

		this.setChanged();
		this.notifyObservers();
	}

	// Méthodes dépréciées ou inutilisées

	@Override
	public void recevoir(boolean accuseDeConnexion) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void recevoirUtilisateur(Set<Utilisateur> tousLesUtilisateurs) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void recevoirMessage(Message message) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void recevoirMessage(Set<Message> tousLesMessages) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void recevoirTicket(Set<Ticket> tousLesTickets) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	@Deprecated
	public void recevoirReponseSQL(String reponse) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void recevoirMessageInvalide(Object messageInvalide) {
		// System.err.println("Message invalide reçu: " + messageInvalide);
	}

}
