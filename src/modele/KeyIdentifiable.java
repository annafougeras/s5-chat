/**
 * Fichier KeyIdentifiable.java
 * @date 9 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

/**
 * 
 */
public class KeyIdentifiable extends AbstractIdentifiable {

	private static final long serialVersionUID = 2413861824016851475L;

	/**
	 * @param uniqueId Identifiant unique (numérique)
	 */
	public KeyIdentifiable(Integer uniqueId) {
		super(uniqueId);
	}

	/**
	 * @param uniqueId Identifiant unique (alphanumérique)
	 */
	public KeyIdentifiable(String uniqueId) {
		super(uniqueId);
	}

}
