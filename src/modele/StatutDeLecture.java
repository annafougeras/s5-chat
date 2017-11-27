/**
 * Fichier StatutDeLecture.java
 * @date 24 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

/**
 * Différentes valeurs du statut que peut avoir un message ou
 * un utilisateur vis-à-vis d'un message
 */
public enum StatutDeLecture {

	NON_ENVOYE,
	ENVOYE,
	RECU,
	LU;
	
	public String toString() {
		return "[Statut de lecture " + name().toLowerCase() + "]";
	}
	
}
