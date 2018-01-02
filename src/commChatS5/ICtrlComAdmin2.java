/**
 * Fichier ICtrlComAdmin2.java
 * @date 2 janv. 2018
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import modele.Identifiable;

/**
 * Version courte du contrôleur admin. 
 * Par exemple, pour ajouter un nouvel utilisateur, on utilise :
 * executer(TAction.INSERER, TData.UTILISATEUR, user);
 */
public interface ICtrlComAdmin2 {
	
	/**
	 * Les types de données sur lesquels il est possible d'agir
	 * ICtrlComAdmin2
	 */
	public static enum TData {
		UTILISATEUR,
		MESSAGE,
		TICKET,
		GROUPE;
	}
	
	/**
	 * Les actions réalisables
	 * ICtrlComAdmin2
	 */
	public static enum TAction {
		INSERER,
		MODIFIER,
		SUPPRIMER,
		OBTENIR;
	}
	


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
	 * Exécuter une action sur un des types de données
	 * @param action
	 * @param data
	 * @param element
	 */
	public void executer(TAction action, TData data, Identifiable element);

	/**
	 * Se déconnecter
	 */
	public void deconnecter();
}
