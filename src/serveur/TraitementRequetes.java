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
			else { 
				idUser = sql.sqlConnexion(nom, pass);
				ok = (idUser >= 0);
			}
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
		
		System.out.println("TR.creationTicket - idUser="+idUser);
		
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

		if (!estAdmin(admin))
			return null;
		
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
		
		Set<Utilisateur> set = null;
		try {
			set = sql.sqlSelectUtilisateurs();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return set;
	
	}

	@Override
	public Message adminDemandeMessage(ComAdresse admin, Identifiable idMessage) {
		
		if (!estAdmin(admin))
			return null;
		
		Message m = null;
		
		try {
			m = sql.sqlSelectMessage(idMessage.getIdentifiantNumeriqueUnique());
		}
		catch (SQLException e) {
			m = null;
		}
		return m;
	}

	@Override
	public Set<Message> adminDemandeMessages(ComAdresse admin) {
		
		if (!estAdmin(admin))
			return null;
		
		Set<Message> set = null;
		
		try {
			set = sql.sqlSelectMessages();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
		
	}

	@Override
	public Ticket adminDemandeTicket(ComAdresse admin, Identifiable idTicket) {
		
		if (!estAdmin(admin))
			return null;
		
		Ticket t = null;
		
		try {
			t = sql.sqlSelectTicket(idTicket.getIdentifiantNumeriqueUnique(), -1);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return t;
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
		//TODO quoi faire ?
		return null;
	}

	@Override
	public Set<Groupe> adminDemandeGroupes(ComAdresse admin) {

		if (!estAdmin(admin))
			return null;
		
		Set<Groupe> set = null;
		
		try {
			set = sql.sqlSelectGroupes(-1);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return set;
	}

	@Override
	public TreeMap<Groupe, NavigableSet<Utilisateur>> adminDemandeUtilisateursParGroupe(ComAdresse admin) {
		
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

	
	
	
	@Override
	public Utilisateur adminSetUtilisateur(ComAdresse admin, Utilisateur utilisateur) {
		
		// TODO Mot de passe et nickname

		if (!estAdmin(admin)) {
			System.err.println("Un client exécute des requêtes admin");
			return null;
		}
		
		int idUtilisateur = utilisateur.getIdentifiantNumeriqueUnique();
		Utilisateur u = null;

		try {
			if (idUtilisateur > 0) 
				idUtilisateur = sql.sqlUpdateUtilisateur(
						idUtilisateur, 
						utilisateur.getNom(), 
						utilisateur.getPrenom(), 
						utilisateur.getNickname(), 
						"root1");
			else
				idUtilisateur = sql.sqlInsertUtilisateur(
						utilisateur.getNom(), 
						utilisateur.getPrenom(), 
						utilisateur.getNickname(), 
						"root1");
			if (idUtilisateur > 0)
				u = sql.sqlSelectUtilisateur(idUtilisateur);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
	}

	@SuppressWarnings("unused")
	@Override 
	@Deprecated
	public Message adminSetMessage(ComAdresse admin, Message message) {
		
		//TODO Supprimer cette fonction

		if (!estAdmin(admin)) {
			System.err.println("Un client exécute des requêtes admin");
			return null;
		}
		
		int idMsg = message.getIdentifiantNumeriqueUnique();
		Message msg = null;

		try {
			int idTicket = message.getParent().getIdentifiantNumeriqueUnique();
			int idUser = message.getEmetteur().getIdentifiantNumeriqueUnique();
			if (idMsg > 0) 
				System.err.println("TraitementRequete (admin): modif message: Non implémenté");
				//idMsg = sql.sqlUpdateMessage(idMsg);
			else
				System.err.println("ADMIN ne peut pas créer de tickets");
			if (idMsg > 0)
				msg = sql.sqlSelectMessage(idMsg);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			System.err.println("Le parent du ticket est : " + message.getParent());
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public Ticket adminSetTicket(ComAdresse admin, Ticket ticket) {

		if (!estAdmin(admin)) {
			System.err.println("Un client exécute des requêtes admin");
			return null;
		}
		
		int idTicket = ticket.getIdentifiantNumeriqueUnique();
		Ticket t = null;

		try {
			int idGroupe = ticket.getParent().getIdentifiantNumeriqueUnique();
			if (idTicket > 0) 
				idTicket = sql.sqlUpdateTicket(idTicket, ticket.getTitre(), idGroupe);
			else
				System.err.println("ADMIN ne peut pas créer de tickets");
			if (idTicket > 0)
				t = sql.sqlSelectTicket(idTicket);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			System.err.println("Le parent du ticket est : " + ticket.getParent());
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public Groupe adminSetGroupe(ComAdresse admin, Groupe groupe) {

		if (!estAdmin(admin)) {
			System.err.println("Un client exécute des requêtes admin");
			return null;
		}
		
		int idGroupe = groupe.getIdentifiantNumeriqueUnique();
		Groupe g = null;;

		try {
			if (idGroupe > 0) 
				idGroupe = sql.sqlUpdateGroupe(idGroupe, groupe.getNom());
			else
				idGroupe = sql.sqlInsertGroupe(groupe.getNom());
			
			if (idGroupe > 0) {
				//TODO En attendant l'implémentation de sqlSelectGroupe
				//g = sql.sqlSelectGroupe(idGroupe, -1);
				
				NavigableSet<Groupe> groupes = sql.sqlSelectGroupes(-1);
				for (Groupe grp: groupes)
					if (grp.getIdentifiantNumeriqueUnique() == idGroupe)
						g = grp;	
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return g;
	}

	
	
	

	@Override
	public void adminRejoindreGroupe(ComAdresse admin, Identifiable idGroupe,
			Identifiable idUtilisateur) {
		int idUser = Integer.parseInt(idUtilisateur.getIdentifiantUnique());
		int idGrp = Integer.parseInt(idGroupe.getIdentifiantUnique());
		try {
			sql.sqlRejoindreGroupe(idUser, idGrp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void adminQuitterGroupe(ComAdresse admin, Identifiable idGroupe,
			Identifiable idUtilisateur) {
		int idUser = idUtilisateur.getIdentifiantNumeriqueUnique();
		int idGrp = idGroupe.getIdentifiantNumeriqueUnique();
		try {
			sql.sqlQuitterGroupe(idUser, idGrp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	@Override
	public void adminSupprimerUtilisateur(ComAdresse admin, Identifiable idUtilisateur) {
		
		if (!estAdmin(admin)) {
			System.err.println("Un non-admin demande à supprimer !");
		}
		else {
			try {
				sql.deleteGroupe(idUtilisateur.getIdentifiantNumeriqueUnique());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void adminSupprimerMessage(ComAdresse admin, Identifiable idMessage) {
		
		if (!estAdmin(admin)) {
			System.err.println("Un non-admin demande à supprimer !");
		}
		else {
			try {
				sql.deleteGroupe(idMessage.getIdentifiantNumeriqueUnique());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void adminSupprimerTicket(ComAdresse admin, Identifiable idTicket) {
		
		if (!estAdmin(admin)) {
			System.err.println("Un non-admin demande à supprimer !");
		}
		else {
			try {
				sql.deleteGroupe(idTicket.getIdentifiantNumeriqueUnique());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void adminSupprimerGroupe(ComAdresse admin, Identifiable idGroupe) {
		
		if (!estAdmin(admin)) {
			System.err.println("Un non-admin demande à supprimer !");
		}
		else {
			try {
				sql.deleteGroupe(idGroupe.getIdentifiantNumeriqueUnique());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	

}
