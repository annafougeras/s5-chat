/**
 * Fichier ControleurComServeur.java
 * @date 28 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication;

import java.io.IOException;
import java.util.Set;


/**
 * Contrôleur de communication serveur permettant de gérer les connexions de
 * multiples clients.
 * 
 * @see ControleurComClient
 * @see ObservateurComServeur
 */
public interface ControleurComServeur<MSG> {
	
	/**
	 * Branchement au réseau
	 * @param port Port d'écoute du contrôleur réseau
	 * @throws ControleurComException
	 */
	public void start(int port) throws ComException ;
	
	/**
	 * Débranchement du réseau
	 */
	public void stop() throws ComException;
	
	/**
	 * Envoyer un message à un client
	 * @param message Message à envoyer
	 * @param destinataire Adresse du client
	 */
	public void informer(MSG message, ComAdresse destinataire);
	
	/**
	 * Envoyer un message à tous les clients connectés
	 * @param message Message à envoyer
	 */
	public void informerTous(MSG message);
	
	/**
	 * Envoyer une requête à un client.
	 * La réponse sera apportée par UserComServeur.recevoir
	 * @param message Requête à envoyer
	 * @param destinataire Adresse du client
	 * 
	 * @see ObservateurComServeur
	 */
	public void demander(MSG message, ComAdresse destinataire);

	/**
	 * Envoyer une requête à tous les clients connectés
	 * Les réponses seront apportées par UserComServeur.recevoir
	 * @param message Requête à envoyer
	 * 
	 * @see ObservateurComServeur
	 */
	public void demanderTous(MSG message);
	
	/**
	 * Déconnecter un client
	 * @param client Le client à déconnecter
	 * @throws IOException
	 */
	public void deconnecter(ComAdresse client);
	
	/**
	 * Donne l'ensemble de tous les clients
	 * @return Set de tous les clients
	 */
	public Set<ComAdresse> tousLesClients();
	
	
}
