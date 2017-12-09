/**
 * Fichier ICtrlComServeur.java
 * @date 5 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.util.Set;

import modele.Identifiable;
import modele.Message;
import modele.Ticket;

import communication.ComAdresse;

/**
 * Interface pour contrôleur de communication (serveur) répondant aux requêtes de
 * liste des groupes et de ticket
 */
public interface ICtrlComServeur {

	/**
	 * Démarre le serveur
	 */
	public void start();
	
	/**
	 * Stoppe le serveur
	 */
	public void stop();
	
	
	
	/**
	 * Obtenir la liste de tous les clients connectés
	 * @return Ensemble de toutes les adresse client
	 */
	public Set<ComAdresse> getClientsConnectes();
	
	

	/**
	 * Informer un client d'un nouveau ticket
	 * @param client Adresse du client à informer
	 * @param nouveauTicket Nouveau ticket
	 */
	public void informer(ComAdresse client, Ticket nouveauTicket);
	
	/**
	 * Informer un client d'un nouveau message
	 * @param client Adresse du client
	 * @param nouveauMessage Nouveau message
	 * @param referenceTicket Référence du ticket sur lequel est posté le message
	 */
	public void informer(ComAdresse client, Message nouveauMessage, Identifiable referenceTicket);
	
	
	
}
