/**
 * Fichier SimpleControleurClient.java
 * @date 30 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication.simple;

import java.io.IOException;
import java.net.Socket;

import communication.ComAdresse;
import communication.ComEtatDeConnexion;
import communication.ComException;
import communication.ComIdentification;
import communication.ControleurComClient;
import communication.ObservateurComClient;
import communication.simple.SimpleMessage.SimpleMessageConfirmationConnexion;
import communication.simple.SimpleMessage.SimpleMessageConnexion;

/**
 * Contrôleur client manipulant des SimpleMessages
 * @see SimpleMessage
 */
public class SimpleControleurClient extends AbstractSimpleControleur implements ControleurComClient<SimpleMessage> {
	
	private ComAdresse serveur;
	private Socket socket;
	private ComEtatDeConnexion etatCnx;
	private ObservateurComClient<SimpleMessage> observateur;
	
	/**
	 * Constructeur
	 * @param observateur Objet à informer lors de la réception de message
	 */
	public SimpleControleurClient(ObservateurComClient<SimpleMessage> observateur){
		this.observateur = observateur;
		this.etatCnx = ComEtatDeConnexion.NON_CONNECTE;
	}

	@Override
	public void connecter(ComAdresse serveurDistant,
			ComIdentification identifiant) {
		
		this.serveur = serveurDistant;
		Thread thread = new Thread(new OuvrirConnexion(identifiant));
		thread.start();
	}

	@Override
	public ComEtatDeConnexion connecterBloquant(ComAdresse serveurDistant,
			ComIdentification identifiant) throws ComException {

		this.serveur = serveurDistant;
		OuvrirConnexion oo = new OuvrirConnexion(identifiant);
		return oo.ouvrirConnexion();
		
	}

	@Override
	public ComEtatDeConnexion getEtatDeConnexion() {
		// TODO Auto-generated method stub
		return etatCnx;
	}

	@Override
	public void informer(SimpleMessage message) throws ComException {
		try {
			assert(message.getTypeMessage()==SimpleTypeMessage.INFORME);
			envoyerMessage(socket, message);
		} catch (AssertionError e){
			throw new ComException.TypeMessageException(e);
		} catch (IOException e) {
			throw new ComException(e);
		}
	}

	@Override
	public void demander(SimpleMessage question) {
		// TODO Auto-generated method stub

	}

	@Override
	public SimpleMessage demanderBloquant(SimpleMessage question) throws ComException {
		SimpleMessage msg = null;
		try {
			assert(question.getTypeMessage()==SimpleTypeMessage.DEMANDE);
			
			envoyerMessage(socket, question);
			msg = recevoirMessage(socket);
			
		} catch (AssertionError e){
			throw new ComException.TypeMessageException(e);
		} catch (IOException e) {
			throw new ComException(e);
		}
		return msg;
	}

	@Override
	public void deconnecter() throws ComException {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * Ouverture de connexion
	 * SimpleControleurClient
	 */
	private class OuvrirConnexion implements Runnable {
		
		private ComIdentification identite;
		
		public OuvrirConnexion(ComIdentification identite){
			this.identite = identite;
		}

		public ComEtatDeConnexion ouvrirConnexion() throws ComException {
			etatCnx = ComEtatDeConnexion.EN_COURS_DE_CONNEXION;
			
			// Etablissement de la connexion TCP
			try {
				socket = new Socket(serveur.getAdresse(), serveur.getPort());
			}
			catch (IOException e){
				etatCnx = ComEtatDeConnexion.NON_CONNECTE;
				throw new ComException("Echec création socket", e);
			}
			if (verbeux)
				System.out.println("Connexion TCP établie avec " + SimpleAdresse.distante(socket));
			
			try {
				
				ouvrirFlux(socket);
				
				// Envoi des identifiants	
				envoyerMessage(socket, new SimpleMessageConnexion(identite));
				
				// Réception de la confirmation de connexion
				SimpleMessageConfirmationConnexion msg = (SimpleMessageConfirmationConnexion) recevoirMessage(socket);
				if (msg.getSuccesConnexion())
					etatCnx = ComEtatDeConnexion.CONNECTE;
				else
					etatCnx = ComEtatDeConnexion.CONNEXION_REFUSEE;
				
				
			} catch (IOException e) {
				etatCnx = ComEtatDeConnexion.NON_CONNECTE;
				throw new ComException("Echec création flux de connexion", e);
			} catch (ClassCastException e) {
				etatCnx = ComEtatDeConnexion.NON_CONNECTE;
				throw new ComException.TypeMessageException("Réception message invalide", e);
			}
			
			return etatCnx;
		}
		
		@Override
		public void run() {
			try {
				ouvrirConnexion();
			}
			catch (ComException e){
				System.err.println("Erreur : " + e.getMessage());
				observateur.ctrlCom_connexionEtablie(false);
			}
		}
		
	}
	
	
	/**
	 * Réception de message
	 * SimpleControleurClient
	 */
	public class ReceptionMessage implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	

}
