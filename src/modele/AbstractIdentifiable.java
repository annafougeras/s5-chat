/**
 * Fichier AbstractIdentifiable.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

/**
 * Objet implémentant les méthodes de l'interface Identifiable
 */
public class AbstractIdentifiable implements Identifiable {
	
	private String uniqueId; 
	private int uniqueNumId;
	
	/**
	 * @param uniqueId Identifiant unique (au sein de la classe)
	 */
	public AbstractIdentifiable(Integer uniqueId) {
		this.uniqueId = uniqueId.toString();
		this.uniqueNumId = uniqueId;
	}
	
	/**
	 * @param uniqueId Identifiant unique (au sein de la classe)
	 */
	public AbstractIdentifiable(String uniqueId) {
		this.uniqueId = uniqueId;
		this.uniqueNumId = uniqueId.hashCode();
	}

	
	@Override
	public String getIdentifiantUnique() {
		return uniqueId;
	}

	@Override
	public int getIdentifiantNumeriqueUnique() {
		return uniqueNumId;
	}
	

	
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(getClass()))
			return getIdentifiantUnique() == ((Identifiable)obj).getIdentifiantUnique();
		return false;
	}
	
	
	@Override
	public String toString() {
		return "[" + getClass().getCanonicalName() + " id=" + getIdentifiantUnique() + "]";
	}
	
	/**
	 * Facilité de toString
	 * @param aConcatener Objets à toStringuer avec l'identifiant
	 * @return Un beau 'toString'
	 */
	protected String toString(String... aConcatener) {
		StringBuilder str = new StringBuilder();
		str.append(getClass().getCanonicalName());
		str.append(" id=" + getIdentifiantUnique());
		for (String chaine: aConcatener)
			str.append(" " + chaine);
		return "[" + str.toString() + "]";
	}

}
