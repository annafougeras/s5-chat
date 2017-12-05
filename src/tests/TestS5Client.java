/**
 * Fichier TestS5Client.java
 * @date 5 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package tests;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import modele.Groupe;
import modele.Ticket;

import commChatS5.CtrlComClient;
import commChatS5.ICtrlComClient;
import commChatS5.Identifiants;
import commChatS5.S5Client;
import communication.simple.SimpleAdresse;

/**
 * Test d'un client du ChatS5
 */
public class TestS5Client {

	/**
	 * Client (qui possède un contrôleur de communication)
	 * TestS5Client
	 */
	public static class Client implements S5Client {
		
		private ICtrlComClient controleur;
		
		/**
		 * Constructeur du client
		 * @param adresseServeur Adresse du serveur
		 * @param portServeur Port du serveur
		 */
		public Client(String adresseServeur, int portServeur){
			controleur = new CtrlComClient(
					this, 
					new SimpleAdresse(adresseServeur, portServeur)
					);
		}

		
		/**
		 * Scénario en mode bloquant : 
		 *   - Demande de connexion
		 *   - Demande de la liste des groupes
		 *   - Demande du ticket 1
		 */
		public void scenarioTestBloquant(){
			System.out.println("\n## Mode bloquant");
			
			boolean b = controleur.etablirConnexionBloquant(
					new Identifiants("nom", "pass")
					);
			System.out.println("Connexion : " + b);
			System.out.println(controleur.demanderTousLesGroupesBloquant());
			System.out.println(controleur.demanderTicketBloquant(1));
			controleur.deconnecter();
		}

		
		/**
		 * Scénario en mode non bloquant : 
		 *   - Demande de connexion
		 * La suite du scénario est appelée lors de l'acquittement de connexion
		 */
		public void demarrerScenarioTest(){
			System.out.println("\n## Mode non bloquant");
			controleur.etablirConnexion(new Identifiants("nom", "pass"));
		}
		
		/**
		 * Scénario en mode non bloquant, suite
		 */
		public void scenarioTest(){
			controleur.demanderTousLesGroupes();
			controleur.demanderTicket(1);
		}
		

		@Override
		public void recevoir(Ticket ticketRecu) {
			System.out.println("Ticket reçu : " + ticketRecu);
		}

		@Override
		public void recevoir(Set<Groupe> listeDesGroupes) {
			System.out.println("Tous les groupes reçus : ");
			for (Iterator<Groupe> iter = listeDesGroupes.iterator(); iter.hasNext();){
				System.out.println(iter.next() + ", ");
			}
		}

		@Override
		public void recevoir(boolean accuseConnexion) {
			System.out.println("Accusé connexion reçu : " + accuseConnexion);
			this.scenarioTest();
		}

		@Override
		public void recevoir(Object messageInconnu) {
			System.out.println("Message inconnu reçu : " + messageInconnu);
		}
		
	}
	
	
	
	/**
	 * Main : création du Client et scénario bloquant et non bloquant.
	 * @param args
	 */
	public static void main(String args[]){
		
		if (args.length != 2){
			System.err.println("Arguments attendus : adresseServeur portServeur");
			System.exit(1);
		}
		
		String adresseServeur = args[0];
		int portServeur = Integer.parseInt(args[1]);
		
		
		Client client = new Client(adresseServeur, portServeur);
		client.scenarioTestBloquant();
		
		
		
		client.demarrerScenarioTest();
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		scan.nextLine();
		
	}
	
	
	
}
