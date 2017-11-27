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
	
	private int uniqueId; 
	
	/**
	 * @param uniqueId Identifiant unique (au sein de la classe)
	 */
	public AbstractIdentifiable(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	/* (non-Javadoc)
	 * @see modele.Identifiable#getIdentifiantUnique()
	 */
	@Override
	public int getIdentifiantUnique() {
		return uniqueId;
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
	public String toString(String... aConcatener) {
		StringBuilder str = new StringBuilder();
		str.append(getClass().getCanonicalName());
		str.append(" id=" + getIdentifiantUnique());
		for (String chaine: aConcatener)
			str.append(" " + chaine);
		return "[" + str.toString() + "]";
	}

}
