/**
 * Fichier ICtrlComAdmin.java
 * @date 9 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import modele.Groupe;
import modele.Identifiable;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

/**
 * Contrôleur pour l'administrateur.
 * Il peut tout faire :-)
 */
public interface ICtrlComAdmin {

	/**
	 * Etablissement de connexion en mode bloquant
	 * @return VRAI si la connexion st établie
	 */
	public boolean etablirConnexionBloquant(Identifiants identifiants);
	
	/**
	 * Etablissement de connexion en mode non bloquant.
	 * Le retour est transmis par S5Admin.recevoir
	 */
	public void etablirConnexion(Identifiants identifiants);
	
	
	

	/**
	 * Demande tous les utilisateurs
	 * Le retour est transmis par S5Admin.recevoirUtilisateur
	 */
	public void demanderUtilisateurs();
	
	/**
	 * Demande tous les utilisateurs par groupe (Map<Groupe,Utilisateur>)
	 * Le retour est transmis par S5Admin.recevoirUtilisateur
	 */
	public void demanderUtilisateursParGroupe();
	
	/**
	 * Demande tous les messages
	 * Le retour est transmis par S5Admin.recevoirMessage
	 */
	public void demanderMessages();
	
	/**
	 * Demande tous les tickets
	 * Le retour est transmis par S5Admin.recevoirTicket
	 */
	public void demanderTickets();
	
	/**
	 * Demande tous les groupes
	 * Le retour est transmis par S5Admin.recevoirGroupe
	 */
	public void demanderGroupes();

	
	
	
	/**
	 * Insère un utilisateur
	 * @param modUtilisateur L'utilisateur modifié / nouveau (remplace si id existant, crée sinon)
	 */
	public void insererUtilisateur(Utilisateur modUtilisateur);
	
	/**
	 * Insère un message
	 * @param modMessage Le message modifié / nouveau(remplace si id existant, crée sinon)
	 */
	public void insererMessage(Message modMessage);
	
	/**
	 * Insère un ticket
	 * @param modTicket Le ticket modifié / nouveau (remplace si id existant, crée sinon)
	 */
	public void insererTicket(Ticket modTicket);
	
	/**
	 * Insère un groupe
	 * @param modGroupe Le groupe modifié / nouveau (remplace si id existant, crée sinon)
	 */
	public void insererGroupe(Groupe modGroupe);
	
	
	
	
	/**
	 * Supprime un utilisateur
	 * @param element Element ayant l'identifiant de l'utilisateur (ou l'utilisateur lui-même)
	 */
	public void supprimerUtilisateur(Identifiable element);
	
	/**
	 * Supprime un message
	 * @param element Element ayant l'identifiant du message (ou le message lui-même)
	 */
	public void supprimerMessage(Identifiable element);
	
	/**
	 * Supprime un Ticket
	 * @param element Element ayant l'identifiant du ticket (ou le ticket lui-même)
	 */
	public void supprimerTicket(Identifiable element);
	
	/**
	 * Supprime un groupe
	 * @param element Element ayant l'identifiant du groupe (ou le groupe lui-même)
	 */
	public void supprimerGroupe(Identifiable element);
	
	
	
	/**
	 * Exécute une requête SQL en mode bloquant
	 * @param commandeSQL La commande SQL à exécuter
	 * @return Le retour SQL
	 */
	@Deprecated
	public String executerSQLBloquant(String commandeSQL);
	
	/**
	 * Exécute une commande SQL en mode non bloquant
	 * @param commandeSQL La commande SQL a exécuter
	 * Le retour est transmis par S5Admin.recevoirAcqSQL
	 */
	@Deprecated
	public void executerSQL(String commandeSQL);
	
	
	/**
	 * Se déconnecter
	 */
	public void deconnecter();
	
}
