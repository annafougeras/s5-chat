/**
 * Fichier LacunaireException.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

/**
 * Exception levée lorsqu'on accède à un champ non renseigné d'un 
 * objet Completable incomplet
 */
public class LacunaireException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public LacunaireException() {
		super();
	}
	
	public LacunaireException(String message) {
		super(message);
	}
	
	public LacunaireException(Throwable cause) {
		super(cause);
	}
	
	public LacunaireException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public LacunaireException(PotentiellementLacunaire objet, String nomGetter) {
		super("Getter '" + nomGetter + "' sur " + objet + " pourtant incomplet");
	}
	
	public LacunaireException(PotentiellementLacunaire objet, String nomGetter, Throwable cause) {
		super("Getter '" + nomGetter + "' sur " + objet + " pourtant incomplet", cause);
	}
}
