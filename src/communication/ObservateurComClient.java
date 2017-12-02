/**
 * Fichier ObservateurComClient.java
 * @date 29 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication;


/**
 * Interface pour les classes souhaitant utiliser un ControleurComClient.
 * 
 * Les méthodes définies permettent de recevoir les informations d'un contrôleur client.
 */
public interface ObservateurComClient<MSG> {
	
	/**
	 * L'appel informe l'utilisateur du contrôleur com du succès/échec 
	 * de la connexion.
	 * 
	 * @param succes VRAI si la connexion est établie, FAUX sinon
	 */
	public void ctrlCom_connexionEtablie(boolean succes);
	
	/**
	 * Recevoir un message du contrôleur réseau
	 * @param message Le message reçu par le contrôleur
	 */
	public void ctrlCom_recevoir(MSG message);

}
