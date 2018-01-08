/**
 * Fichier Serveur.java
 * @date 31 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package serveur;

import java.util.Date;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import modele.Identifiable;
import modele.KeyIdentifiable;
import modele.Message;
import modele.StatutDeLecture;
import modele.Ticket;
import modele.Utilisateur;

import commChatS5.CtrlComServeur;
import commChatS5.ICtrlComServeur;
import commChatS5.S5Serveur;
import communication.ComAdresse;

/**
 * 
 */
public class Serveur {
	
	public static int port;
	public static ICtrlComServeur ctrlCom;
	public static S5Serveur traitReq;
	
	public static void main(String args[]){
		
		if (args.length < 1){
			System.err.println("Arguments attendus : \n\t - port du serveur \n\t - [local|distant]");
			System.exit(1);
		}
		
		// DB locale ou distante
		boolean local = true;
		if (args.length >= 2){
			switch (args[1]){
			case "distant":
				local = false;
				break;
			default:
				local = true;
				break;
			}
		}
		
		port = Integer.parseInt(args[0]);
		traitReq = new Instance(local);
		ctrlCom = new CtrlComServeur(traitReq, port);
		
		System.out.println("Démarrage du serveur");
		ctrlCom.start();
		
		
		// Attente de commandes
		Scanner scan = new Scanner(System.in);
		boolean quitter = false;
		do {
			String cmd = scan.nextLine();
			switch (cmd){
			
			// Ligne vide
			case "":
				break;
			
			// Liste des clients connectés
			case "list":
			case "clients":
				System.out.println("Clients connectés :");
				try {
					Iterator<ComAdresse> iter = ctrlCom.getClientsConnectes().iterator();
					for (;iter.hasNext();)
						System.out.println(iter.next());
				}
				catch (NullPointerException e){
					System.err.println("Pas implémenté !");
				}
				break;
				
			// Créer un message fictif
			case "new m":
				Message msg = new Message(0, new Utilisateur("michelID", "Michoux", "Michel"),"Nouveau message !",new Date(), new TreeMap<Utilisateur,StatutDeLecture>());
				Identifiable ticket = new KeyIdentifiable(0);
				Identifiable groupe = new KeyIdentifiable(0);
				ticket.setParent(groupe);
				msg.setParent(ticket);
				
				for (ComAdresse client: ctrlCom.getClientsConnectes())
					ctrlCom.informer(client, msg, ticket);
					
				break;
			
			// Créer un ticket fictif
			case "new t":
				Message msg1 = new Message(-10, new Utilisateur("marcelID", "Marçoux", "Marcel"), "Premier message du nouveau ticket", new Date(), new TreeMap<Utilisateur,StatutDeLecture>());
				NavigableSet<Message> messages = new TreeSet<>();
				messages.add(msg1);
				Ticket t = new Ticket(-100, "Nouveau ticket", messages, new Date());
				t.setParent(new KeyIdentifiable(0));
				
				for (ComAdresse client: ctrlCom.getClientsConnectes())
					ctrlCom.informer(client, t);
				
				break;
			
			// Quitter
			case "exit":
			case "quit":
				quitter = true;
				break;
				
			// Défaut
			default:
				System.out.println("La commande "+cmd+" n'est pas reconnue");
				System.out.println("Essayez list / new m / new t / quit");
				break;
			}
		} while (!quitter);
		
		scan.close();
		
		ctrlCom.stop();
		System.out.println("Au revoir !");
		
		
	}

}
