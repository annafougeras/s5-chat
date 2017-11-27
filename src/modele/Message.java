/**
 * Fichier Message.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

import java.util.Date;

/**
 * Message émis par un Utilisateur sur un Ticket
 */
public class Message extends AbstractIdentifiable implements Comparable<Message> {

	private String texte = null;
	private Utilisateur emetteur = null;
	private Date dateEmission;
	private StatutDeLecture statutUtilisateur;
	private StatutDeLecture statutGeneral;
	
	
	
	/**
	 * Crée un message complet
	 * @param uniqueId Identifiant unique
	 * @param emetteur Émetteur 
	 * @param texte Contenu
	 * @param date Date d'émission
	 * @param statutGeneral Statut de lecture du message
	 * @param statutUtilisateur Statut de lecture de l'utilisateur
	 */
	public Message(int uniqueId, Utilisateur emetteur, String texte, 
			Date date, StatutDeLecture statutGeneral, StatutDeLecture statutUtilisateur) {
		super(uniqueId);
		setEmetteur(emetteur);
		setTexte(texte);
		setDateEmission(date);
		setStatutGeneral(statutGeneral);
		setStatutUtilisateur(statutUtilisateur);
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
	 * Obtenir le statutUtilisateur
	 * @return statutUtilisateur
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


	/**
	 * Obtenir le statutGeneral
	 * @return statutGeneral
	 */
	public StatutDeLecture getStatutGeneral() {
		return statutGeneral;
	}


	/**
	 * Définir le statutGeneral
	 * @param statutGeneral Le statutGeneral à définir
	 */
	public void setStatutGeneral(StatutDeLecture statutGeneral) {
		this.statutGeneral = statutGeneral;
	}


	
	
	@Override
	public int compareTo(Message autre) {
		int cmp = dateEmission.compareTo(autre.dateEmission);

		if (cmp == 0)
			cmp = getIdentifiantUnique() - autre.getIdentifiantUnique();
		return cmp;
	}
	
	// equals dans AbstractIdentifiable

	@Override
	public int hashCode() {
		return 37 * getDateEmission().hashCode() * getIdentifiantUnique();
	}
	
	@Override
	public String toString() {
		return super.toString(
				"date_emission=" + dateEmission
				);
	}

	
	
	
}
