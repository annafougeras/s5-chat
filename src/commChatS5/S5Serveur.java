/**
 * Fichier S5Serveur.java
 * @date 5 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.util.Set;

import modele.Groupe;
import modele.Ticket;

import communication.ComAdresse;

/**
 * Interface pour utilisateur du contrôleur de communication serveur (listener)
 */
public interface S5Serveur {

	/**
	 * Demande de connexion à acquiescer
	 * @param client Adresse du client
	 * @param identifiants Identifiants du client
	 * @return VRAI si la connexion est acceptée, FAUX sinon
	 */
	public boolean demandeConnexion(ComAdresse client, Identifiants identifiants);
	
	/**
	 * Demande de la liste des groupes par un client
	 * @param client Adresse du client
	 * @return Ensemble de tous les groupes
	 */
	public Set<Groupe> demandeTousLesGroupes(ComAdresse client);
	
	/**
	 * Demande d'un ticket par un client
	 * @param client Adresse du client
	 * @param idTicket Identifiant du ticket demandé
	 * @return Le ticket demandé (ou null en cas de refus)
	 */
	public Ticket demandeTicket(ComAdresse client, int idTicket);
	
}
