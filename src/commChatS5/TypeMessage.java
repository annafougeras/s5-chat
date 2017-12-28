/**
 * Fichier TypeMessage.java
 * @date 4 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

/**
 * Types de messages nécessaires aux contrôleurs ChatS5
 */
public enum TypeMessage {
	REQUETE_LISTE_GROUPE,
	REQUETE_TICKET,
	REQUETE_NOUVEAU_TICKET,
	REQUETE_NOUVEAU_MESSAGE,
	REQUETE_ADMIN,
	
	INFORME_LISTE_GROUPE,
	INFORME_TICKET,
	INFORME_MESSAGE,
	INFORM_ADMIN,
	
	INCONNU;
}
