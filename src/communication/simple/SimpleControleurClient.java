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
	
	public static int NB_ESSAIS = 5;
	
	private ComAdresse serveur;
	private Socket socket;
	private ComEtatDeConnexion etatCnx;
	private ObservateurComClient<SimpleMessage> observateur;
	
	private int essaisRestants = NB_ESSAIS;
	
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
		Thread thread = new Thread(new Requete(question));
		thread.start();
	}

	@Override
	public SimpleMessage demanderBloquant(SimpleMessage question) throws ComException {
		Requete req = new Requete(question);
		return req.executerRequete();
	}

	@Override
	public void deconnecter() {
		fermerFlux();
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
			boolean accuse;
			try {
				ComEtatDeConnexion etat = ouvrirConnexion();
				accuse = (etat == ComEtatDeConnexion.CONNECTE);
			}
			catch (ComException e){
				System.err.println("Erreur : " + e.getMessage());
				accuse = false;
			}
			observateur.ctrlCom_connexionEtablie(accuse);
		}
		
	}
	
	
	/**
	 * Demande d'informations
	 * SimpleControleurClient
	 */
	public class Requete implements Runnable {
		
		private SimpleMessage question;
		
		public Requete(SimpleMessage question){
			this.question = question;
		}

		public SimpleMessage executerRequete() throws ComException {
			SimpleMessage reponse = null;
			boolean recu = false;
			while (!recu && essaisRestants > 0){
				try {
					assert(question.getTypeMessage()==SimpleTypeMessage.DEMANDE);
					
					envoyerMessage(socket, question);
					reponse = recevoirMessage(socket);
					
					assert(reponse.getTypeMessage()==SimpleTypeMessage.INFORME);
					
					recu = true;
					essaisRestants = NB_ESSAIS;

				} catch (AssertionError e){
					essaisRestants--;
					if (essaisRestants < 0)
						throw new ComException.TypeMessageException("Le message n'est pas une requête", e);
					else{
						System.out.println("Echec assertion : " + e.getMessage() + " : essai supplémentaire");
						e.printStackTrace();
					}
				} catch (IOException e){
					essaisRestants--;
					if (essaisRestants < 0)
						throw new ComException("IOException ", e);
					else
						System.out.println("Echec IO : " + e.getMessage() + " : essai supplémentaire");
				}
			}
			System.out.println(reponse);
			return reponse;
		}
		
		@Override
		public void run() {
			SimpleMessage reponse;
			try {
				reponse = executerRequete();
			} catch (ComException e) {
				e.printStackTrace();
				reponse = new SimpleMessage.SimpleMessageInvalide();
				
			}
			observateur.ctrlCom_recevoir(reponse);
		}
		
	}
	

}
