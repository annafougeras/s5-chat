/**
 * Fichier TestComSimpleClient.java
 * @date 1 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package tests;

import java.util.Scanner;

import communication.ComAdresse;
import communication.ComException;
import communication.ControleurComClient;
import communication.ObservateurComClient;
import communication.simple.SimpleAdresse;
import communication.simple.SimpleControleurClient;
import communication.simple.SimpleIdentification;
import communication.simple.SimpleMessage;
import communication.simple.SimpleMessage.SimpleMessageDemande;
import communication.simple.SimpleMessage.SimpleMessageInformation;

/**
 * Utilisation d'un contrôleur client en mode bloquant
 */
public class TestComSimpleClient {

	
	/**
	 * Observateur : celui qui manipule le contrôleur
	 * TestComSimpleClient
	 */
	private static class ObservateurClient implements ObservateurComClient<SimpleMessage> {

		private ControleurComClient<SimpleMessage> controleurReseau;
		
		public ObservateurClient(){
			controleurReseau = new SimpleControleurClient(this);
		}
		
		/**
		 * Demande au contrôleur de se connecter
		 * @param adresse
		 * @param port
		 */
		public void seConnecter(String adresse, int port){
			ComAdresse adr = new SimpleAdresse(adresse, port);
			try {
				controleurReseau.connecterBloquant(adr, new SimpleIdentification("user", "pass"));
			} catch (ComException e) {
				System.err.println("connexion: "+e.getMessage());
			}
			
			System.out.println("Connexion: " + controleurReseau.getEtatDeConnexion());
		}

		/**
		 * Demande au contrôleur d'envoyer un message
		 */
		public void envoyerInformations() {
			System.out.println("Envoi info serveur");
			SimpleMessage msg = new SimpleMessageInformation("mes infos sont...");
			try {
				controleurReseau.informer(msg);
			} catch (ComException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Demande au contrôleur d'envoyer une requête
		 */
		public void demanderInformations() {
			System.out.println("Demande info serveur");
			SimpleMessage msg = new SimpleMessageDemande("Je voudrais...");
			SimpleMessage reponse = null;
			try {
				reponse = controleurReseau.demanderBloquant(msg);
			} catch (ComException e) {
				e.printStackTrace();
			}
			System.out.println("Réponse obtenue : " + reponse);
		}

		/**
		 * To be continued...
		 */
		public void seDeconnecter() {
			// TODO Auto-generated method stub
			
		}
		
		
		@Override
		public void ctrlCom_connexionEtablie(boolean succes) {
		}

		@Override
		public void ctrlCom_recevoir(SimpleMessage message) {
		}
		
	}
	
	
	
	
	
	public static void main(String args[]){

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
		if (args.length != 2){
			System.err.println("arguments: adresse_serveur port_serveur");
			scan.nextLine();
			System.exit(1);
		}
		
		String adresseServeur = args[0];
		int portServeur = Integer.parseInt(args[1]);
		
		
		// Scénario
		
		
		ObservateurClient client = new ObservateurClient();
		
		try {
			client.seConnecter(adresseServeur, portServeur);
			
			Thread.sleep(400);
			
			client.envoyerInformations();
			
			Thread.sleep(700);
			
			client.demanderInformations();
			
			Thread.sleep(1000);
			
			client.seDeconnecter();
			
	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("fin");
		scan.nextLine();

	}
	
	
}
