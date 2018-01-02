/**
 * Fichier TraitementRequetes.java
 * @date 31 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package serveur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import modele.Groupe;
import modele.Identifiable;
import modele.Message;
import modele.StatutDeLecture;
import modele.Ticket;
import modele.Utilisateur;

import commChatS5.Identifiants;
import commChatS5.S5Serveur;
import communication.ComAdresse;

/**
 * 
 */
public class TraitementRequetes implements S5Serveur {
	public static int uniqueID = 1000;
	
	// Association adresse réseau / nom d'utilisateur
	private static Map<ComAdresse,String> utilisateurs;
	
	// Données fictives
	public static Utilisateur[] data_users;
	public static Message[] data_messages;
	public static Ticket[] data_tickets;
	public static Groupe[] data_groups;
	public static Map<String,List<Groupe>> appartenance;
	
	
	
	public TraitementRequetes(){
		utilisateurs = new HashMap<>();
		initDonneesFictives();
	}

	
	private void initDonneesFictives(){
		/*
		 * 5 groupes
		 * 10 utilisateurs appartenant au groupe uid%5
		 * 10 tickets (un par chaque utilisateur) crée sur uid-1%10
		 * 20 réponses (une par utilisateur et par ticket)
		 */
		
		NavigableMap<Utilisateur, StatutDeLecture> toutLu = new TreeMap<>();
		appartenance = new HashMap<>();
		
		data_groups = new Groupe[5];
		data_groups[0] = new Groupe(0, "Tout le monde");
		for (int i = 1; i < 5; ++i)
			data_groups[i] = new Groupe(i, "Groupe "+i);
		
		data_tickets = new Ticket[10];
		for (int i = 0; i < 10; ++i) {
			data_tickets[i] = new Ticket(i, "Ticket "+i, 0, new Date());
			data_tickets[i].setParent(data_groups[i%5]);
		}
		
		data_users = new Utilisateur[10];
		for (int i = 0; i < 10; ++i) {
			data_users[i] = new Utilisateur(""+i, "User_"+i, "Marcel");
			String strUser = data_users[i].getIdentifiantUnique();
			appartenance.put(strUser, new ArrayList<Groupe>());
			appartenance.get(strUser).add(data_groups[0]);
			appartenance.get(strUser).add(data_groups[i%4 + 1]);
			appartenance.get(strUser).add(data_groups[(i+1)%4 + 1]);
		}
		
		data_messages = new Message[20];
		for (int i = 0; i < 10; ++i)
			data_messages[i] = new Message(i, data_users[i], "Coucou n°"+i+" !", new Date(), toutLu);
		
		for (int i = 10; i < 20; ++i)
			data_messages[i] = new Message(i, data_users[i-10], "Réponse n°"+i+"", new Date(), toutLu);
		
		// Ajout des messages sur les tickets
		for (int i = 0; i < 20; ++i)
			data_tickets[i%10].addMessage(data_messages[i]);
		
	}
	
	
	
	
	
	
	
	@Override
	public boolean demandeConnexion(ComAdresse client, Identifiants identifiants) {
		// TODO Vérification identifiants
		System.out.println(
				"Connexion acceptée (" + 
				identifiants.getNom() + ", " + 
				(String) identifiants.getPass() + ")"
				);
		utilisateurs.put(client, identifiants.getNom());
		return true;
	}
	
	

	@Override
	public Set<Groupe> demandeTousLesGroupes(ComAdresse client) {
		// TODO Groupes
		
		
		List<Groupe> groupes = Arrays.asList(data_groups);
		
		String user = utilisateurs.get(client);

		// Modifie les tickets pour coller à l'utilisateur
		// Attention, juste pour les tests avec 1 utilisateur
		// car les modifs sont durables et seront envoyées aussi aux autres
		if (appartenance.containsKey(user)){
			
			Iterator<Groupe> iter = appartenance.get(user).iterator();
			for (;iter.hasNext();) {
				Groupe g = iter.next();
				for (Ticket t: data_tickets)
					if (t.getParent().equals(g))
						g.addTicketConnu(t);
			}
		}
		
		return new HashSet<Groupe>(groupes);
	}

	@Override
	public Ticket demandeTicket(ComAdresse client, Identifiable idTicket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ticket creationTicket(ComAdresse client, Identifiable groupe,
			String titre, String premierMessage) {
		// TODO Le ticket n'est pas ajouté mais simplement renvoyé
		
		TreeSet<Message> messages = new TreeSet<>();
		messages.add(
			new Message(
					1000,
					new Utilisateur(""+(uniqueID++), utilisateurs.get(client), "M."),
					premierMessage + "\n(Le message n'est pas enregistré sur le serveur)",
					new Date(),
					new TreeMap<Utilisateur,StatutDeLecture>()
					)
			);
		Ticket t = new Ticket(
			uniqueID++, 
			titre, 
			messages,
			new Date());
		t.setParent(groupe);
		
		return t;
	}

	@Override
	public Message creationMessage(ComAdresse client, Identifiable ticket,
			String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur adminDemandeUtilisateur(ComAdresse admin,
			Identifiable idUtilisateur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Utilisateur> adminDemandeUtilisateurs(ComAdresse admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message adminDemandeMessage(ComAdresse admin, Identifiable idMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Message> adminDemandeMessages(ComAdresse admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ticket adminDemandeTicket(ComAdresse admin, Identifiable idTicket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Ticket> adminDemandeTickets(ComAdresse admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Groupe adminDemandeGroupe(ComAdresse admin, Identifiable idGroupe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Groupe> adminDemandeGroupes(ComAdresse admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void adminSetUtilisateur(ComAdresse admin, Utilisateur utilisateur) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adminSetMessage(ComAdresse admin, Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adminSetTicket(ComAdresse admin, Ticket ticket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adminSetGroupe(ComAdresse admin, Groupe groupe) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adminSupprimerUtilisateur(ComAdresse admin,
			Identifiable idUtilisateur) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adminSupprimerMessage(ComAdresse admin, Identifiable idMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adminSupprimerTicket(ComAdresse admin, Identifiable idTicket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adminSupprimerGroupe(ComAdresse admin, Identifiable idGroupe) {
		// TODO Auto-generated method stub

	}

}
