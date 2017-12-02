/**
 * Fichier ObservateurComServeur.java
 * @date 29 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication;

/**
 * Interface pour les classes souhaitant utiliser un ControleurComServeur.
 * 
 * Les méthodes définies permettent de recevoir des informations d'un contrôleur serveur.
 * 
 * @see ControleurComServeur
 */
public interface ObservateurComServeur<MSG extends ComMessage> {
	
	/**
	 * Le contrôleur demande si les identifiants sont valides
	 * @param client Adresse du client demandant la connexion
	 * @param identifiants Les identifiants
	 * @return VRAI si les identifiants sont valides, FAUX sinon
	 */
	public boolean ctrlCom_validerConnexion(ComAdresse client, ComIdentification identifiants);
	
	/**
	 * Recevoir un message du réseau (sans réponse)
	 * @param client Adresse du client émetteur du message
	 * @param message
	 */
	public void ctrlCom_informer(ComAdresse client, MSG message);
	
	/**
	 * Recevoir une requête du réseau (avec réponse)
	 * @param client Adresse du client émetteur de la requête
	 * @param requete Requête
	 * @return Réponse
	 */
	public ComMessage ctrlCom_recevoir(ComAdresse client, MSG requete);

}
