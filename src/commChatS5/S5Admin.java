/**
 * Fichier S5Admin.java
 * @date 9 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.util.Set;

import modele.Groupe;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

/**
 * Interface à implémenter par l'administrateur (qui peut tout recevoir)
 */
public interface S5Admin {

	/**
	 * Recevoir l'accusé de connexion
	 * @param accuseDeConnexion VRAI si la connexion est établie
	 */
	public void recevoir(boolean accuseDeConnexion);

	
	
	
	/**
	 * Recevoir un utilisateur
	 * @param utilisateur
	 */
	public void recevoirUtilisateur(Utilisateur utilisateur);
	
	/**
	 * Recevoir tous les utilisateurs
	 * @param tousLesUtilisateurs
	 */
	public void recevoirUtilisateur(Set<Utilisateur> tousLesUtilisateurs);
	
	
	
	/**
	 * Recevoir un message
	 * @param message
	 */
	public void recevoirMessage(Message message);
	
	/**
	 * Recevoir tous les messages
	 * @param tousLesMessages
	 */
	public void recevoirMessage(Set<Message> tousLesMessages);

	
	
	/**
	 * Recevoir un ticket
	 * @param ticket
	 */
	public void recevoirTicket(Ticket ticket);
	
	/**
	 * Recevoir tous les tickets
	 * @param tousLesTickets
	 */
	public void recevoirTicket(Set<Ticket> tousLesTickets);
	
	
	
	
	/**
	 * Recevoir un groupe
	 * @param groupe
	 */
	public void recevoirGroupe(Groupe groupe);
	
	/**
	 * Recevoir tous les groupes
	 * @param tousLesGroupes
	 */
	public void recevoirGroupe(Set<Groupe> tousLesGroupes);
	
	
	@Deprecated
	public void recevoirReponseSQL(String reponse);
	
}
