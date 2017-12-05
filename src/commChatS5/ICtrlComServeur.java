/**
 * Fichier ICtrlComServeur.java
 * @date 5 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

/**
 * Interface pour contrôleur de communication (serveur) répondant aux requêtes de
 * liste des groupes et de ticket
 */
public interface ICtrlComServeur {

	/**
	 * Démarre le serveur
	 */
	public void start();
	
	/**
	 * Stoppe le serveur
	 */
	public void stop();
	
}
