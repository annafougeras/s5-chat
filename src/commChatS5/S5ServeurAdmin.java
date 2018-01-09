/**
 * Fichier S5AdminServeur.java
 * @date 23 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;

import modele.Groupe;
import modele.Identifiable;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

import communication.ComAdresse;

/**
 * Interface pour utilisateur du contrôleur de communication serveur (listener)
 * Partie communication client-admin
 */
public interface S5ServeurAdmin {

	
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
	 * Demande tous les utilisateurs rangés par groupes
	 * @param client
	 * @return
	 */
	TreeMap<Groupe,NavigableSet<Utilisateur>> adminDemandeUtilisateursParGroupe(ComAdresse client);


	
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
	public Utilisateur adminSetUtilisateur(ComAdresse admin, Utilisateur utilisateur);	

	/**
	 * Ajouter / modifier un message
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param message Le message à ajouter (nouvel id) ou a modifier (id existant)
	 */
	@Deprecated
	public Message adminSetMessage(ComAdresse admin, Message message);	

	/**
	 * Ajouter / modifier un ticket
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param ticket Le ticket à ajouter (nouvel id) ou a modifier (id existant)
	 */
	public Ticket adminSetTicket(ComAdresse admin, Ticket ticket);	

	/**
	 * Ajouter / modifier un groupe
	 * @param admin Adresse de l'administrateur effectuant la demande
	 * @param groupe Le groupe à ajouter (nouvel id) ou a modifier (id existant)
	 */
	public Groupe adminSetGroupe(ComAdresse admin, Groupe groupe);

	
	
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
	
}
