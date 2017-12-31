/**
 * Fichier Serveur.java
 * @date 31 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package serveur;

import java.util.Iterator;
import java.util.Scanner;

import commChatS5.CtrlComServeur;
import commChatS5.ICtrlComServeur;
import communication.ComAdresse;

/**
 * 
 */
public class Serveur {
	
	public static int port;
	public static ICtrlComServeur ctrlCom;
	public static TraitementRequetes traitReq;
	
	public static void main(String args[]){
		
		if (args.length != 1){
			System.err.println("Argument attendu : port du serveur");
			System.exit(1);
		}
		
		port = Integer.parseInt(args[0]);
		traitReq = new TraitementRequetes();
		ctrlCom = new CtrlComServeur(traitReq, port);
		
		System.out.println("Démarrage du serveur");
		ctrlCom.start();
		
		
		// Attente de commandes
		Scanner scan = new Scanner(System.in);
		boolean quitter = false;
		do {
			String cmd = scan.nextLine();
			switch (cmd){
			
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
			
			case "exit":
			case "quit":
				quitter = true;
				break;
				
			default:
				System.out.println("La commande "+cmd+" n'est pas reconnue");
				break;
			}
		} while (!quitter);
		
		scan.close();
		
		ctrlCom.stop();
		System.out.println("Au revoir !");
		
		
	}

}
