/**
 * Fichier AbstractPotentiellementLacunaire.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

/**
 * Les objets implémentant l'interface Completable peuvent
 * être instanciés comme "objets imcomplets" : ils présentent
 * alors des lacunes dans leurs données.
 */
public interface PotentiellementLacunaire extends Identifiable {

	/**
	 * Affirme qu'un objet est complet
	 * @return VRAI si l'objet est complet, FAUX s'il est incompet
	 */
	public boolean estComplet();
	
	/**
	 * Définit la complétude d'un objet
	 * @param complet VRAI si l'objet est complet, FAUX sinon
	 */
	public void setComplet(boolean complet);
	
}
