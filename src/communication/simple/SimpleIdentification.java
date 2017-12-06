/**
 * Fichier SimpleIdentification.java
 * @date 30 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication.simple;

import communication.ComIdentification;

/**
 * Contient un couple identifiant / mot de passe.
 * Attention le mot de passe est envoyé tel que transmis lors de l'initialisation
 */
@SuppressWarnings("serial")
public class SimpleIdentification implements ComIdentification {
	
	private String nom;
	private String pass; //< En clair !
	
	/**
	 * Constructeur
	 * @param nom Nom d'utilisateur
	 * @param pass Mot de passe tel qu'envoyé sur le réseau
	 */
	public SimpleIdentification(String nom, String pass){
		this.nom = nom;
		this.pass = pass;
	}

	/**
	 * Obtenir le nom
	 * @return Le nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Obtenir le pass
	 * @return Le pass
	 */
	public String getPass() {
		return pass;
	}
	
	@Override
	public String toString(){
		return "[identite]";
	}
	

}
