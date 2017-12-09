/**
 * Fichier S5Client.java
 * @date 4 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.util.Set;

import modele.Groupe;
import modele.Message;
import modele.Ticket;

/**
 * Interface pour utilisateur de contrôler de communication client (listener)
 */
public interface S5Client {
	
	/**
	 * Recevoir un ticket en mode non bloquant
	 * @param ticketRecu Le ticket reçu
	 */
	public void recevoir(Ticket ticketRecu);
	
	/**
	 * Recevoir la liste des groupes en mode non bloquant
	 * @param listeDesGroupes Tous les groupes
	 */
	public void recevoir(Set<Groupe> listeDesGroupes);
	
	/**
	 * Recevoir l'accusé de connexion en mode non bloquant
	 * @param accuseConnexion VRAI si la connexion est acceptée
	 */
	public void recevoir(boolean accuseConnexion);
	
	/**
	 * Recevoir un nouveau message
	 * @param messageRecu
	 */
	public void recevoir(Message messageRecu);
	
	/**
	 * Recevoir un message invalide
	 * @param messageInconnu null 
	 */
	public void recevoir(Object messageInconnu);
	
}
