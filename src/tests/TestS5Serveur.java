/**
 * Fichier TestS5Serveur.java
 * @date 5 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package tests;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import modele.Groupe;
import modele.Identifiable;
import modele.KeyIdentifiable;
import modele.Message;
import modele.StatutDeLecture;
import modele.Ticket;
import modele.Utilisateur;

import commChatS5.CtrlComServeur;
import commChatS5.ICtrlComServeur;
import commChatS5.Identifiants;
import commChatS5.S5ServeurClient;
import communication.ComAdresse;

/**
 * Test du serveur S5Chat
 */
public class TestS5Serveur {

	/**
	 * Serveur (fake) TestS5Serveur
	 */
	public static class Serveur implements S5ServeurClient {

		public static int compteur = 0;

		private static int cptClient = 0;
		private Map<ComAdresse, Utilisateur> clients = new HashMap<>();
		private ICtrlComServeur controleur;

		/**
		 * Constructeur
		 * 
		 * @param port
		 *            Port d'écoute (local)
		 */
		public Serveur(int port) {
			System.err.println("Ce test ne fonctionne plus depuis que le serveur doit gérer les admins");
			// ci-dessous : null au lieu de this
			controleur = new CtrlComServeur(null, port);
		}

		/**
		 * Scénario de test : le serveur est simplement à l'écoute
		 */
		public void scenarioTest() {
			controleur.start();
		}

		@Override
		public boolean demandeConnexion(ComAdresse client, Identifiants identifiants) {
			System.out.println("Connexion demandée : " + client + " " + identifiants);
			clients.put(client, new Utilisateur(""+cptClient, client.getAdresse(), "" + client.getPort()));
			return true;
		}

		@Override
		public Set<Groupe> demandeTousLesGroupes(ComAdresse client) {
			Set<Groupe> set = new TreeSet<>();
			for (int i = 0; i < 10; ++i)
				set.add(new Groupe(i, "Groupe " + i));
			return set;
		}

		@Override
		public Ticket demandeTicket(ComAdresse client, Identifiable idTicket) {
			NavigableSet<Message> messages = new TreeSet<>();
			messages.add(new Message(1, new Utilisateur("1", "Dupont", "Michel"), "Mon message", new Date(),
					new TreeMap<Utilisateur, StatutDeLecture>()));
			return new Ticket(idTicket.getIdentifiantNumeriqueUnique(),
					"Ticket " + idTicket.getIdentifiantNumeriqueUnique(), new TreeSet<Message>(), new Date());
		}

		@Override
		public Ticket creationTicket(ComAdresse client, Identifiable groupe, String titre, String premierMessage) {

			System.out.println("Création de ticket par " + client);
			NavigableSet<Message> messages = new TreeSet<>();
			messages.add(new Message(compteur++, clients.get(client), premierMessage, new Date(),
					new TreeMap<Utilisateur, StatutDeLecture>()));
			return new Ticket(compteur++, titre, messages, new Date());

		}

		@Override
		public Message creationMessage(ComAdresse client, Identifiable ticket, String message) {

			System.out.println("Création de message par " + client);
			Message msg = new Message(compteur++, clients.get(client), message, new Date(),
					new TreeMap<Utilisateur, StatutDeLecture>());
			msg.setParent(new KeyIdentifiable(1000));

			// Informe les autres clients du nouveau message
			for (ComAdresse adresse : controleur.getClientsConnectes())
				if (adresse != client)
					controleur.informer(adresse, msg, new KeyIdentifiable(1000));

			return msg;
		}

		@Override
		public void informeStatutDeLecture(ComAdresse client, Identifiable idMsg, StatutDeLecture statut) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Main : démarrage du serveur
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		if (args.length != 1) {
			System.err.println("Arguments: portServeur");
			System.exit(1);
		}

		int port = Integer.parseInt(args[0]);

		Serveur serveur = new Serveur(port);
		serveur.scenarioTest();

		// Reconnaît les commandes 'list' et 'exit' dans la console
		Scanner scan = new Scanner(System.in);
		for (String ligne = scan.nextLine(); !ligne.equals("exit"); ligne = scan.nextLine()) {
			switch (ligne) {
			case "list":
				System.out.println(serveur.controleur.getClientsConnectes());
				break;
			case "":
				break;
			default:
				System.out.println("Commande reconnues : list | exit");
			}
		}
		System.out.println("fini");
		scan.close();
		serveur.controleur.stop();

	}

}
