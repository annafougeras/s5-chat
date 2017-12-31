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
import java.util.NavigableSet;
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
	
	// Association adresse réseau / nom d'utilisateur
	private static Map<ComAdresse,String> utilisateurs;
	
	// Données fictives
	public static Utilisateur[] data_users;
	public static Message[] data_messages;
	public static Ticket[] data_tickets;
	public static Groupe[] data_groups;
	
	
	
	
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
		
		data_groups = new Groupe[5];
		for (int i = 0; i < 5; ++i)
			data_groups[i] = new Groupe(i, "Groupe "+i);
		
		data_tickets = new Ticket[5];
		for (int i = 0; i < 5; ++i)
			data_tickets[i] = new Ticket(i, "Ticket "+i, 0, new Date());
		
		data_users = new Utilisateur[10];
		for (int i = 0; i < 10; ++i)
			data_users[i] = new Utilisateur(""+i, "User_"+i, "Marcel");
		
		data_messages = new Message[20];
		for (int i = 0; i < 10; ++i)
			data_messages[i] = new Message(i, data_users[i], "Coucou !", new Date(), toutLu);
		
		for (int i = 10; i < 20; ++i)
			data_messages[i] = new Message(i, data_users[i-10], "Réponse", new Date(), toutLu);
	}
	
	
	
	
	
	
	
	@Override
	public boolean demandeConnexion(ComAdresse client, Identifiants identifiants) {
		// TODO Vérification identifiants
		System.out.println(
				"Connexion acceptée (" + 
				identifiants.getNom() + ", " + 
				identifiants.getPass() + ")"
				);
		utilisateurs.put(client, identifiants.getNom());
		return true;
	}
	
	

	@Override
	public Set<Groupe> demandeTousLesGroupes(ComAdresse client) {
		// TODO Groupes
		
		// Données fictives : 5 groupes nommés 1, 2, 3, 4 et 5
		
		List<Groupe> groupes = Arrays.asList(data_groups);
		
		// Si l'utilisateur est 'azer', il peut lire les tickets 1, 2, 3 sur les groupes 1 et 2
		if (utilisateurs.get(client).equals("azer")){
			NavigableSet<Ticket> tickets = new TreeSet<>();
			tickets.add(data_tickets[0]);
			tickets.add(data_tickets[1]);
			groupes.get(0).addTicketsConnus(data_tickets[0], data_tickets[1]);
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
		// TODO Auto-generated method stub
		return null;
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
