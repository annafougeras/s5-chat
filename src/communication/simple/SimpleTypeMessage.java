/**
 * Fichier SimpleTypeMessage.java
 * @date 30 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication.simple;

import java.io.Serializable;

/**
 * Types de messages pouvant être identifiés dans un SimpleMessage
 */
public enum SimpleTypeMessage implements Serializable {

	BONJOUR,
	IDENTIFICATION, 
	CONFIRM_IDENTIFICATION,
	INFORME,
	DEMANDE,
	INVALIDE;
	
}
