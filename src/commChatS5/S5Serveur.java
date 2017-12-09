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
import modele.Identifiable;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

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
	public Ticket demandeTicket(ComAdresse client, Identifiable idTicket);
	
	
	
	/**
	 * Création d'un nouveau ticket par un client
	 * @param client Adresse du client
	 * @param groupe Groupe destinataire du ticket
	 * @param titre Titre du ticket
	 * @param premierMessage Contenu du premier message
	 * @return Le ticket créé
	 */
	public Ticket creationTicket(ComAdresse client, Identifiable groupe, String titre, String premierMessage);

	/**
	 * Création d'un nouveau message par un client
	 * @param client Adresse du client
	 * @param ticket Référence du ticket sur lequel le message est posté
	 * @param message Contenu du message
	 * @return Le message créé
	 */
	public Message creationMessage(ComAdresse client, Identifiable ticket, String message);
	

	
	
	
	
	
	
	
	
	
	
	// Admin
	
	/**
	 * Demande un utilisateur
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param idUtilisateur Identifiant de l'utilisateur demandé
	 * @return L'utilisateur demandé
	 */
	public Utilisateur adminDemandeUtilisateur(ComAdresse admin, Identifiable idUtilisateur);
	
	/**
	 * Demande tous les utilisateurs
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @return Ensemble de tous les utilisateurs
	 */
	public Set<Utilisateur> adminDemandeUtilisateurs(ComAdresse admin);	


	
	/**
	 * Demande un message
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param idMessage Identifiant du message demandé
	 * @return Le message demandé
	 */
	public Message adminDemandeMessage(ComAdresse admin, Identifiable idMessage);
	
	/**
	 * Demande tous les messages
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @return Ensemble de tous les messages
	 */
	public Set<Message> adminDemandeMessages(ComAdresse admin);	



	/**
	 * Demande un ticket
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param idTicket Identifiant du ticketdemandé
	 * @return Le ticket demandé
	 */
	public Ticket adminDemandeTicket(ComAdresse admin, Identifiable idTicket);
	
	/**
	 * Demande tous les tickets
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @return Ensemble de tous les tickets
	 */
	public Set<Ticket> adminDemandeTickets(ComAdresse admin);

	

	/**
	 * Demande un groupe
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param idGroupe Identifiant du groupe message demandé
	 * @return Le groupe demandé
	 */
	public Groupe adminDemandeGroupe(ComAdresse admin, Identifiable idGroupe);
	
	/**
	 * Demande tous les groupes
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @return Ensemble de tous les groupes
	 */
	public Set<Groupe> adminDemandeGroupes(ComAdresse admin);	
	
	

	/**
	 * Ajouter / modifier un utilisateur
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param utilisateur L'utilisateur à ajouter (nouvel id) ou a modifier (id existant)
	 */
	public void adminSetUtilisateur(ComAdresse admin, Utilisateur utilisateur);	

	/**
	 * Ajouter / modifier un message
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param message Le message à ajouter (nouvel id) ou a modifier (id existant)
	 */
	public void adminSetMessage(ComAdresse admin, Message message);	

	/**
	 * Ajouter / modifier un ticket
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param ticket Le ticket à ajouter (nouvel id) ou a modifier (id existant)
	 */
	public void adminSetTicket(ComAdresse admin, Ticket ticket);	

	/**
	 * Ajouter / modifier un groupe
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param groupe Le groupe à ajouter (nouvel id) ou a modifier (id existant)
	 */
	public void adminSetGroupe(ComAdresse admin, Groupe groupe);

	
	
	/**
	 * Supprimer un utilisateur
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param idUtilisateur ID de l'utilisateur
	 */
	public void adminSupprimerUtilisateur(ComAdresse admin, Identifiable idUtilisateur);
	
	/**
	 * Supprimer un message
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param idMessage ID du message
	 */
	public void adminSupprimerMessage(ComAdresse admin, Identifiable idMessage);
	
	/**
	 * Supprimer un ticket
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param idTicket ID du ticket
	 */
	public void adminSupprimerTicket(ComAdresse admin, Identifiable idTicket);
	
	/**
	 * Supprimer un groupe
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param idGroupe ID du groupe
	 */
	public void adminSupprimerGroupe(ComAdresse admin, Identifiable idGroupe);
	
	
	
	/**
	 * Exécute une commande SQL
	 * @param admin Adresse de l'administrateur effectuant la requête
	 * @param commandeSQL Commande SQL à exécuter
	 * @return Résultat de la commande SQL
	 */
	public String executerSQL(ComAdresse admin, String commandeSQL);
	
}
