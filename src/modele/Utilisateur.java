/**
 * Fichier Utilisateur.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

/**
 * 
 */
public class Utilisateur extends AbstractIdentifiable {

	private String nom;
	
	/**
	 * @param uniqueId Identifiant unique au sein des utilisateurs
	 * @param nom Nom
	 */
	public Utilisateur(int uniqueId, String nom) {
		super(uniqueId);
		this.nom = nom;
	}

	/**
	 * Obtenir le nom
	 * @return nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Définir le nom
	 * @param nom Le nom à définir
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	
	// equals dans AbstractIdentifiable
	
	@Override
	public int hashCode() {
		return 29 * getNom().hashCode() * getIdentifiantUnique();
	}

}
