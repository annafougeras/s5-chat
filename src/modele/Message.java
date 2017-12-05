/**
 * Fichier Message.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;

/**
 * Message émis par un Utilisateur sur un Ticket
 */
public class Message extends AbstractIdentifiable implements Comparable<Message> {

	private static final long serialVersionUID = 7293458972472166742L;
	private String texte = null;
	private Utilisateur emetteur = null;
	private Date dateEmission;
	private NavigableMap<Utilisateur,StatutDeLecture> statuts;
	private StatutDeLecture statutUtilisateur;
	
	
	
	/**
	 * Crée un message complet
	 * @param uniqueId Identifiant unique
	 * @param emetteur Émetteur 
	 * @param texte Contenu
	 * @param date Date d'émission
	 * @param statuts Statuts de lecture du message
	 */
	public Message(int uniqueId, Utilisateur emetteur, String texte, 
			Date date, NavigableMap<Utilisateur,StatutDeLecture> statuts) {
		super(uniqueId);
		setEmetteur(emetteur);
		setTexte(texte);
		setDateEmission(date);
		setStatuts(statuts);
	}


	/**
	 * Obtenir le texte
	 * @return texte
	 */
	public String getTexte() {
		return texte;
	}


	/**
	 * Définir le texte
	 * @param texte Le texte à définir
	 */
	public void setTexte(String texte) {
		this.texte = texte;
	}


	/**
	 * Obtenir l'émetteur de ce message
	 * @return emetteur
	 */
	public Utilisateur getEmetteur() {
		return emetteur;
	}


	/**
	 * Définir l'émetteur
	 * @param emetteur L'émetteur à définir
	 */
	public void setEmetteur(Utilisateur emetteur) {
		this.emetteur = emetteur;
	}


	/**
	 * Obtenir le dateEmission
	 * @return dateEmission
	 */
	public Date getDateEmission() {
		return dateEmission;
	}


	/**
	 * Définir le dateEmission
	 * @param dateEmission Le dateEmission à définir
	 */
	public void setDateEmission(Date dateEmission) {
		this.dateEmission = dateEmission;
	}

	
	/**
	 * Obtenir les statuts
	 * @return Les statuts
	 */
	public Map<Utilisateur,StatutDeLecture> getStatuts() {
		return statuts;
	}


	/**
	 * Définir les statuts
	 * @param statuts Les statuts à définir
	 */
	public void setStatuts(NavigableMap<Utilisateur,StatutDeLecture> statuts) {
		this.statuts = statuts;
	}




	
	
	/**
	 * Obtenir le statutUtilisateur
	 * @return Le statutUtilisateur
	 */
	public StatutDeLecture getStatutUtilisateur() {
		return statutUtilisateur;
	}


	/**
	 * Définir le statutUtilisateur
	 * @param statutUtilisateur Le statutUtilisateur à définir
	 */
	public void setStatutUtilisateur(StatutDeLecture statutUtilisateur) {
		this.statutUtilisateur = statutUtilisateur;
	}


	@Override
	public int compareTo(Message autre) {
		int cmp = dateEmission.compareTo(autre.dateEmission);

		if (cmp == 0)
			cmp = new IdentifiableComparator().compare(this, autre);
		return cmp;
	}
	
	// equals dans AbstractIdentifiable

	@Override
	public int hashCode() {
		return 37 * getDateEmission().hashCode() * getIdentifiantUnique().hashCode();
	}
	
	@Override
	public String toString() {
		return super.toString(
				"date_emission=" + dateEmission
				);
	}

	
	
	
}
