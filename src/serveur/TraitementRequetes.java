/**
 * Fichier TraitementRequete.java
 * @date 8 janv. 2018
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package serveur;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;

import modele.Groupe;
import modele.Identifiable;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

import commChatS5.Identifiants;
import commChatS5.S5Serveur;
import communication.ComAdresse;

/**
 * 
 */
public class TraitementRequetes implements S5Serveur {
	

	
	// On garde l'association adresse réseau / identifiant
	Map<ComAdresse,Integer> utilisateurs = new HashMap<>();
	Set<ComAdresse> admins = new HashSet<>();
	IInstance sql;

	
	public TraitementRequetes(boolean local) {
		sql = new Instance(local);
	}
	
	public TraitementRequetes() {
		this(true);
	}
	
	
	
	
	/* METHODES PRATIQUES */
	
	private boolean estAdmin(ComAdresse client){
		return admins.contains(client);
	}
	
	
	

	
	
	
	
	
	/* INTERFACE S5SERVEUR */
	
	@Override 
	public boolean demandeConnexion(ComAdresse client, Identifiants identifiants) {
		String nom = identifiants.getNom();
		String pass= identifiants.getPass();
		
		int idUser = -1;
		boolean ok = false;
		boolean estAdmin = false;
		
		try {
			if (nom.equals("admin")){
				estAdmin = (sql.sqlConnexionAdmin(pass) >= 0);
			}
			else
				ok = (sql.sqlConnexion(nom, pass) >= 0);
		}
		catch (SQLException e){
			e.printStackTrace();
			ok = false;
			idUser = -1;
			estAdmin = false;
		}
		
		
		// On garde l'association ComAdresse/id
		if (ok) 
			utilisateurs.put(client, idUser);
		if (estAdmin)
			admins.add(client);
		return (ok || estAdmin);
	}

	@Override 
	public Set<Groupe> demandeTousLesGroupes(ComAdresse client) {
		Set<Groupe> set;
		int utilisateur = utilisateurs.get(client);
		
		try {
			set = sql.sqlSelectGroupes(utilisateur);
		}
		catch (SQLException e){
			e.printStackTrace();
			set = null;
		}
		return set;
	}



	@Override
	public Ticket demandeTicket(ComAdresse client, Identifiable idTicket) {

		Ticket unTicket = null;
		int utilisateur = utilisateurs.get(client);
		int id_ticket = idTicket.getIdentifiantNumeriqueUnique();
		
		try {
			unTicket = sql.sqlSelectTicket(id_ticket, utilisateur);
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return unTicket;
	}


	
	@Override 
	public Ticket creationTicket(ComAdresse client, Identifiable groupe, String titre, String premierMessage) {
		
		int idUser = utilisateurs.get(client);
		int idGroupe = groupe.getIdentifiantNumeriqueUnique();
		int idTicket;
		Ticket t = null;
		
		try {
			idTicket = sql.sqlInsertTicket(titre, premierMessage, idUser, idGroupe);
			if (idTicket >= 0)
				t = sql.sqlSelectTicket(idTicket, idUser);
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return t;
	}

	@Override
	public Message creationMessage(ComAdresse client, Identifiable ticket,String message)  {
		
		int idUser = utilisateurs.get(client);
		int idTicket = ticket.getIdentifiantNumeriqueUnique();
		int idMsg;
		Message m = null;
		
		try {
			idMsg = sql.sqlInsertMessage(message, idUser, idTicket);
			if (idMsg >= 0)
				m = sql.sqlSelectMessage(idMsg);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return m;
	
	}

	@Override
	public Utilisateur adminDemandeUtilisateur(ComAdresse admin, Identifiable idUtilisateur) {
	
		Utilisateur u;
		try {
			u = sql.sqlSelectUtilisateur(utilisateurs.get(idUtilisateur));
		}
		catch (SQLException e) {
			e.printStackTrace();
			u = null;
		}
		return u;
		
	}

	@Override
	public Set<Utilisateur> adminDemandeUtilisateurs(ComAdresse admin) {
		
		if (!estAdmin(admin))
			return null;
		
		
		Set<Utilisateur> set;
		try {
			set = sql.sqlSelectUtilisateurs();
		}
		catch (SQLException e){
			e.printStackTrace();
			set = null;
		}
		return set;
	
	}

	@Override
	public Message adminDemandeMessage(ComAdresse admin, Identifiable idMessage) {
		
		if (!estAdmin(admin))
			return null;
		
		Message m;
		
		try {
			m = sql.sqlSelectMessage(idMessage.getIdentifiantNumeriqueUnique());
		}
		catch (SQLException e) {
			e.printStackTrace();
			m = null;
		}
		return m;
	}

	@Override
	public Set<Message> adminDemandeMessages(ComAdresse admin) {
		
		if (!estAdmin(admin))
			return null;
		
		Set<Message> set;
		
		try {
			set = sql.sqlSelectMessages();
		}
		catch (SQLException e) {
			e.printStackTrace();
			set = null;
		}
		return set;
		
	}

	@Override
	public Ticket adminDemandeTicket(ComAdresse admin, Identifiable idTicket) {
		
		if (!estAdmin(admin))
			return null;
		
		Ticket t;
		
		try {
			t = sql.sqlSelectTicket(idTicket.getIdentifiantNumeriqueUnique(), -1);
		}
		catch (SQLException e) {
			t = null;
		}
		
		return t;
		
		/*
		// PAS DEPLACÉ CAR EN DOUBLE : récupérer un ticket complet
		String sql = "SELECT * FROM ticket WHERE id_ticket ="+ idTicket.getIdentifiantNumeriqueUnique() +" LIMIT 1";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		
		
		// Recuperation des messages pour chaque ticket (juste l'id suffit pour cette méthode)
		NavigableSet<Message> messages = new TreeSet<Message>();
		String sql2 = "SELECT id_message FROM message WHERE id_ticket = " + idTicket.getIdentifiantNumeriqueUnique();

		Statement stmt2 = conn.createStatement();
		ResultSet rs2 = stmt2.executeQuery(sql2);

		while(rs2.next()){
			Message unMessage = new Message(rs2.getInt("id_message"), null, null, null, null);
			messages.add(unMessage);
		}

		Ticket retourne = new Ticket(idTicket.getIdentifiantNumeriqueUnique(), rs.getString("titre_ticket"), messages, rs.getDate("creation_ticket"));
		return retourne;
		*/
	}

	@Override
	public Set<Ticket> adminDemandeTickets(ComAdresse admin) {
		
		Set<Ticket> set = null;
		
		try {
			set = sql.sqlSelectTickets();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	
	
	@Override
	public Groupe adminDemandeGroupe(ComAdresse admin, Identifiable idGroupe) {
		return null;
		
		// Pas trop sûr de quoi faire. Le groupe avec ses tickets ? Liste des membres ?
		
		
		/*
		String sql = "SELECT nom_groupe FROM groupe WHERE id_groupe ="+ idGroupe.getIdentifiantNumeriqueUnique() +" LIMIT 1";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		
		
		// Recuperation des tickets du groupe (l'id seulement là aussi)
		NavigableSet<Ticket> tickets = new TreeSet<Ticket>();
		String sql2 = "SELECT id_ticket FROM ticket WHERE id_groupe  = " + idGroupe.getIdentifiantNumeriqueUnique();

		Statement stmt2 = conn.createStatement();
		ResultSet rs2 = stmt2.executeQuery(sql2);

		while(rs2.next()){
			Ticket t = new Ticket(rs2.getInt("id_ticket"), null, null, null);
			tickets.add(t);
		}

		Groupe retourne = new Groupe(idGroupe.getIdentifiantNumeriqueUnique(), rs.getString("nom_groupe"), tickets);
		return retourne;
		*/
	}

	@Override
	public Set<Groupe> adminDemandeGroupes(ComAdresse admin) {

		if (!estAdmin(admin))
			return null;
		
		Set<Groupe> set;
		
		try {
			set = sql.sqlSelectGroupes(-1);
		}
		catch (SQLException e) {
			set = null;
		}
		
		return set;
		/*
		Set<Groupe> retourne = new TreeSet<>();
		String sql = "SELECT nom_groupe FROM groupe";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		
		while(rs.next()) {
			// Recuperation des tickets des groupes (l'id seulement là aussi)
			NavigableSet<Ticket> tickets = new TreeSet<Ticket>();
			String sql2 = "SELECT id_ticket FROM ticket WHERE id_groupe  = " + rs.getInt("id_groupe");
	
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql2);
	
			while(rs2.next()){
				Ticket t = new Ticket(rs2.getInt("id_ticket"), null, null, null);
				tickets.add(t);
			}
			Groupe g = new Groupe(rs.getInt("id_groupe"), rs.getString("nom_groupe"), tickets);
			retourne.add(g);
		}

		return retourne;
		*/
	}

	@Override // pas de password définit ici
	public Utilisateur adminSetUtilisateur(ComAdresse admin, Utilisateur utilisateur) {
		int idUser = -1;
		Utilisateur u = null;
		if (estAdmin(admin)) {
			try {
				idUser = sql.sqlInsertUtilisateur("on", "a", "pas", "l'info");
				if (idUser >= 0)
					u = sql.sqlSelectUtilisateur(idUser);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.err.println("Un client exécute des requêtes admins");
		}
		
		return u;
	}

	@Override // manque l'id du ticket concerné (manque trop d'infos ou alors j'ai pas compris ce qu'elle doit faire)
	public Message adminSetMessage(ComAdresse admin, Message message) {
		int idMsg = -1;
		Message m = null;;
		if (estAdmin(admin)) {
			try {
				idMsg = sql.sqlInsertMessage("c'est mal fait", 1, 2);
				if (idMsg >= 0)
					m = sql.sqlSelectMessage(idMsg);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.err.println("Un client exécute des requêtes admins");
		}
		return m;
	}

	@Override // manque le groupe
	public Ticket adminSetTicket(ComAdresse admin, Ticket ticket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // ici c'est bon
	public Groupe adminSetGroupe(ComAdresse admin, Groupe groupe) {
		int idGroupe = -1;
		Groupe g = null;;
		if (estAdmin(admin)) {
			try {
				idGroupe= sql.sqlInsertGroupe(groupe.getNom());
				System.out.println(idGroupe);
				if (idGroupe>= 0)
					g = sql.sqlSelectGroupe(idGroupe, -1);
				System.out.println(g);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.err.println("Un client exécute des requêtes admins");
		}
		return g;
		
	}

	@Override
	public void adminSupprimerUtilisateur(ComAdresse admin, Identifiable idUtilisateur) {
	}

	@Override
	public void adminSupprimerMessage(ComAdresse admin, Identifiable idMessage) {
	}

	@Override
	public void adminSupprimerTicket(ComAdresse admin, Identifiable idTicket) {
	}

	@Override
	public void adminSupprimerGroupe(ComAdresse admin, Identifiable idGroupe) {
	}

	@Override
	public TreeMap<Groupe, NavigableSet<Utilisateur>> adminDemandeUtilisateursParGroupe(
			ComAdresse admin) {
		if (!estAdmin(admin))
			return null;
		
		TreeMap<Groupe, NavigableSet<Utilisateur>> map = null;
		try {
			map = sql.sqlSelectUtilisateursParGroupe();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	

}
