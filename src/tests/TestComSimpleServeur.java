/**
 * Fichier TestComSimpleServeur.java
 * @date 2 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package tests;

import java.util.Scanner;

import communication.ComAdresse;
import communication.ComException;
import communication.ComIdentification;
import communication.ComMessage;
import communication.ControleurComServeur;
import communication.ObservateurComServeur;
import communication.simple.SimpleControleurServeur;
import communication.simple.SimpleMessage;
import communication.simple.SimpleMessage.SimpleMessageInformation;


/**
 * Utilisation d'un contrôleur serveur pour traiter des requêtes
 */
public class TestComSimpleServeur {
	
	
	/**
	 * Serveur : il manipule le contrôleur.
	 * Il fournit les réponses après traitement des requêtes
	 * TestComSimpleServeur
	 */
	private static class Serveur implements ObservateurComServeur<SimpleMessage> {
		
		private ControleurComServeur<SimpleMessage> controleurReseau;
		
		public Serveur(){
			controleurReseau = new SimpleControleurServeur(this);
		}
		
		/**
		 * Démarre le contrôleur (attente des connexions entrantes)
		 * @param port
		 */
		public void start(int port){
			try {
				controleurReseau.start(port);
			} catch (ComException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Arrête le contrôleur
		 */
		public void stop(){
			try {
				controleurReseau.stop();
			} catch (ComException e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean ctrlCom_validerConnexion(ComAdresse client,
				ComIdentification identifiants) {
			if (identifiants == null){
				System.out.println("Observateur: Identification " + client + " : identité invalide (null)");
				return false;
			}
			else {
				System.out.println("Observateur: Identification " + client + " : identité valide");
				return true;
			}
		}

		@Override
		public void ctrlCom_informer(ComAdresse client, SimpleMessage message) {
			System.out.println("Observateur : message reçu de " + client + 
					" : " + TestComExtracteurSimpleMessage.getString(message));
		}

		@Override
		public ComMessage ctrlCom_recevoir(ComAdresse client, SimpleMessage requete) {
			System.out.println("Observateur : demande reçue de " + client + 
					" : " + TestComExtracteurSimpleMessage.getString(requete));
			return new SimpleMessageInformation("Les infos du serveur sont...");
		}

		
	}

	
	
	
	public static void main(String args[]){
		
		
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
		if (args.length != 1){
			System.err.println("Arguments : port_local");
			scan.nextLine();
			System.exit(1);
		}
		
		int portLocal = Integer.parseInt(args[0]);
		
		
		// Scénario
		
		
		Serveur serveur = new Serveur();
		
		serveur.start(portLocal);
		
		
		int delai = 5;
		System.out.println(">> Extinction du serveur dans " + delai + " secondes");
		try {
			Thread.sleep(delai*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(">> Extinction du serveur !");
		serveur.stop();
		
		
		System.out.println("fin");
		scan.nextLine();
		
	}
}
