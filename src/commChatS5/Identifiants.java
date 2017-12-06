/**
 * Fichier Identifiants.java
 * @date 4 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import communication.simple.SimpleIdentification;

/**
 * Structure de donnée stockant les identifiants de connexion
 * (mot de passe haché menu menu)
 */
public class Identifiants extends SimpleIdentification {

	private static final long serialVersionUID = -6425343563183736927L;

	/**
	 * @param nom Nom d'utilisateur
	 * @param pass Mot de passe
	 */
	public Identifiants(String nom, String pass) {
		super(nom, encoder(pass));
	}
	
	/**
	 * Hash du mot de passe
	 * @param pass Le mot de passe à hacher
	 * @return Mot de passe en tous petits bouts.
	 */
	public static String encoder(String pass){
		// TODO Faire la fonction d'encodage
		return pass;
	}
}
