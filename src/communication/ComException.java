/**
 * Fichier ComException.java
 * @date 1 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication;


/**
 * Exception liée aux contrôleurs de communication
 */
public class ComException extends Exception {
	
	/**
	 * Exception levée par les messages mal typés du contrôleur de communication.
	 */
	public static class TypeMessageException extends ComException {

		private static final long serialVersionUID = -3006698798643686440L;

		public TypeMessageException() {
			super("Type de message incorrect");
		}

		/**
		 * @param message
		 * @param cause
		 * @param enableSuppression
		 * @param writableStackTrace
		 */
		public TypeMessageException(String message, Throwable cause,
				boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		/**
		 * @param message
		 * @param cause
		 */
		public TypeMessageException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @param message
		 */
		public TypeMessageException(String message) {
			super(message);
		}

		/**
		 * @param cause
		 */
		public TypeMessageException(Throwable cause) {
			super("Type de message incorrect", cause);
		}
		
	}

	private static final long serialVersionUID = -6721143684928144679L;

	/**
	 * exception levée par le contrôleur de communication
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
