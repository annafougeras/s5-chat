/**
 * Fichier ComAdresse.java
 * @date 28 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication;

import java.io.Serializable;


/**
 * Adresse réseau d'un contrôleur client ou serveur
 */
public interface ComAdresse extends Comparable<ComAdresse>, Serializable {
	

	/**
	 * Obtenir l'adresse
	 * @return L'adresse
	 */
	public String getAdresse();

	/**
	 * Obtenir le port
	 * @return Le port
	 */
	public int getPort();

}
