/**
 * Fichier ComException.java
 * @date 1 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication;


/**
 * Exception liée au contrôleurs de communication
 */
public class ComException extends RuntimeException {

	private static final long serialVersionUID = -6721143684928144679L;

	/**
	 * 
	 */
	public ComException() {
		super("Contrôleur communication : exception");
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ComException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ComException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ComException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ComException(Throwable cause) {
		super("Contrôleur communication : exception", cause);
	}
	
}
