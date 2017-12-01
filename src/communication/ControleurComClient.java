/**
 * Fichier ControleurComClient.java
 * @date 28 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication;

import java.io.IOException;

/**
 * Contrôleur client permettant de se connecter à un serveur et d'échanger
 * des messages avec lui.
 * @see ControleurComServeur
 * @see ObservateurComClient
 */
public interface ControleurComClient {
	
	/**
	 * Etablir une connexion avec un serveur distant.
	 * Le bon déroulement est confirmé par UserComClient.ctrlCom_connexionEtablie
	 * 
	 * @param serveurDistant L'adresse du serveur distant
	 * @param identifiant Identifiants de connexion
	 *
	 * @see ObservateurComClient
	 */
	public void connecter(ComAdresse serveurDistant, ComIdentification identifiant);

	
	/**
	 * Etablir une connexion avec un serveur distant. Bloquant
	 * 
	 * @param serveurDistant L'adresse du serveur distant
	 * @param identifiant Identifiants de connexion
	 * @return etat de la connexion
	 * @throws IOException
	 */
	public ComEtatDeConnexion connecterBloquant(ComAdresse serveurDistant, 
			ComIdentification identifiant) throws ComException;
	
	
	
	/**
	 * Obtenir l'état de la connexion avec le serveur
	 * @return Etat de la connexion
	 */
	public ComEtatDeConnexion getEtatDeConnexion();
	
	
	/**
	 * Envoyer un message (sans réponse) au serveur
	 * @param message Le message à envoyer
	 */
	public void informer(ComMessage message);
	
	/**
	 * Envoyer une requête au serveur 
	 * (la réponse sera apportée par UserComClient.ctrlCom_recevoir)
	 * 
	 * @param question La requête à envoyer
	 * @see ObservateurComClient
	 */
	public void demander(ComMessage question);
	
	/**
	 * Envoyer une requête au serveur. Blaquant
	 * 
	 * @param question La requête à envoyer
	 * @return Réponse du serveur
	 * @see ObservateurComClient
	 */
	public ComMessage demanderBloquant(ComMessage question) throws ComException;
	
	/**
	 * Doit être appelé pour fermer proprement le contrôleur
	 * @throws IOException 
	 */
	public void deconnecter() throws IOException;

}
