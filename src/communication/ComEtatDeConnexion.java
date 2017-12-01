/**
 * Fichier ComEtatDeConnexion.java
 * @date 30 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication;

/**
 * États possibles d'une connexion 
 */
public enum ComEtatDeConnexion {
	NON_CONNECTE,
	EN_COURS_DE_CONNEXION,
	CONNECTE,
	DECONNECTE,
	CONNEXION_REFUSEE;
}