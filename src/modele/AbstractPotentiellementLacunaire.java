/**
 * Fichier AbstractPotentiellementLacunaire.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

/**
 * 
 */
public abstract class AbstractPotentiellementLacunaire extends AbstractIdentifiable implements PotentiellementLacunaire {

	
	private static final long serialVersionUID = 7448983757082548846L;
	private boolean complet;
	
	/**
	 * Constructeur
	 * @param uniqueId Identifiant unique
	 */
	public AbstractPotentiellementLacunaire(int uniqueId) {
		super(uniqueId);
	}
	
	/**
	 * Constructeur indiquant la complétude de l'objet
	 * @param uniqueId Identifiant unique
	 * @param complet VRAI si l'objet est complet
	 */
	public AbstractPotentiellementLacunaire(int uniqueId, boolean complet) {
		super(uniqueId);
		setComplet(complet);
	}

	@Override
	public boolean estComplet() {
		return complet;
	}

	@Override
	public void setComplet(boolean complet) {
		this.complet = complet;
	}

}
