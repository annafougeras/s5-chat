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
public class Utilisateur extends AbstractIdentifiable implements Comparable<Utilisateur> {

	private static final long serialVersionUID = 6393296873908620109L;
	private String nom;
	private String prenom;
	private String nickname;
	
	/**
	 * @param uniqueId Identifiant unique au sein des utilisateurs
	 * @param nom Nom
	 * @param prenom Prénom
	 */
	public Utilisateur(String uniqueId, String nom, String prenom) {
		super(uniqueId);
		setNom(nom);
		setPrenom(prenom);
		setNickname(prenom);
	}
	
	/**
	 * @param uniqueId Identifiant unique au sein des utilisateurs
	 * @param nom Nom
	 * @param prenom Prénom
	 * @param nickname Surnom de type abc1234z
	 */
	public Utilisateur(String uniqueId, String nom, String prenom, String nickname) {
		super(uniqueId);
		setNom(nom);
		setPrenom(prenom);
		setNickname(nickname);
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



	/**
	 * Obtenir le prenom
	 * @return Le prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * Définir le prenom
	 * @param prenom Le prenom à définir
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}



	/**
	 * Obtenir le nickname
	 * @return Le nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Définir le nickname
	 * @param nickname Le nickname à définir
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	

	
	// equals dans AbstractIdentifiable
	@Override
	public int hashCode() {
		return 29 * getIdentifiantUnique().hashCode();
	}

	@Override
	public int compareTo(Utilisateur autre) {
		int cmp = getNom().compareTo(autre.getNom());
		if (cmp == 0)
			cmp = getPrenom().compareTo(autre.getPrenom());
		if (cmp == 0)
			cmp = new IdentifiableComparator().compare(this, autre);
		return cmp;
	}

}
