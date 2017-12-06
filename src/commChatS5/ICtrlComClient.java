/**
 * Fichier ICtrlComClient.java
 * @date 4 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.util.Set;

import modele.Groupe;
import modele.Ticket;

/**
 * Interface pour contrôleur de communication authehtifiée (client)
 */
public interface ICtrlComClient {
	
	/**
	 * Etablir la connexion en mode bloquant
	 * @param identifiants Identifiants de connexion
	 * @return VRAI si la connexion est établie, faux sinon
	 */
	public boolean etablirConnexionBloquant(Identifiants identifiants);
	
	/**
	 * Etablir la connexion en mode non bloquant.
	 * L'accusé est transmis par S5Client.recevoir
	 * @param identifiants Identifiants de connexion
	 * 
	 * @see S5Client
	 */
	public void etablirConnexion(Identifiants identifiants);

	
	
	/**
	 * Demander un ticket en mode bloquant
	 * @param idTicket Identifiant unique du ticket
	 * @return Le ticket demandé
	 */
	public Ticket demanderTicketBloquant(int idTicket);
	
	/**
	 * Demander un ticket en mode non bloquant.
	 * Le ticket est transmis par S5Client.recevoir
	 * @param idTicket Identifiant du ticket
	 * 
	 * @see S5Client
	 */
	public void demanderTicket(int idTicket);
	
	
	
	/**
	 * Demander la liste des groupes en mode bloquant
	 * @return Ensemble des groupes
	 */
	public Set<Groupe> demanderTousLesGroupesBloquant();
	
	/**
	 * Demander la liste des groupes en mode non bloquant
	 * La liste est transmise par S5Client.recevoir
	 * 
	 * @see S5Client
	 */
	public void demanderTousLesGroupes();
	
	
	
	/**
	 * Se déconnecter
	 */
	public void deconnecter();
	
	
}
