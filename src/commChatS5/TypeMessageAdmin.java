/**
 * Fichier TypeMessageAdmin.java
 * @date 28 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

/**
 * Types de messages nécessaires au contrôleur admin
 */
public enum TypeMessageAdmin {
	
	UTILISATEUR,
	MESSAGE,
	TICKET,
	GROUPE,

	TOUS_UTILISATEURS,
	TOUS_UTILISATEURS_PAR_GROUPE,
	TOUS_MESSAGES,
	TOUS_TICKETS,
	TOUS_GROUPES,
	
	SUPP_UTILISATEUR,
	SUPP_MESSAGE,
	SUPP_TICKET,
	SUPP_GROUPE,
	
	AJOUT_MODIF_UTILISATEUR,
	AJOUT_MODIF_MESSAGE,
	AJOUT_MODIF_TICKET,
	AJOUT_MODIF_GROUPE,
	
	REJOINDRE_GROUPE,
	QUITTER_GROUPE,
	
	SQL;

}
