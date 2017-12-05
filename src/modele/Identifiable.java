/**
 * Fichier Identifiable.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

import java.io.Serializable;

/**
 * Offre la garantie qu'un objet présente un identifiant
 * numérique (unique au sein de sa classe
 */
public interface Identifiable extends Serializable {
	
	/**
	 * Obtenir l'identifiant unique d'un objet
	 * @return Chaine de caractère unique au sein de la classe
	 */
	public String getIdentifiantUnique();

	
	/**
	 * Obtenir l'identifiant unique d'un objet sous forme numérique
	 * @return Nombre entier unique au sein de la classe
	 */
	public int getIdentifiantNumeriqueUnique();
}
