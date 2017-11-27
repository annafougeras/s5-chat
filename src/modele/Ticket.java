/**
 * Fichier Ticket.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

import java.util.Collection;
import java.util.Date;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * 
 */
public class Ticket extends AbstractPotentiellementLacunaire implements Comparable<Ticket> {
	
	private String titre;
	private NavigableSet<Message> messages;
	private int nbMessagesNonLus = 0;
	private Date dateCreation;
	private Date dateDernierMessage;
	
	
	

	/**
	 * Crée un ticket incomplet
	 * @param uniqueId Identifiant unique
	 */
	public Ticket(int uniqueId, String titre, int nbMessagesNonLus, Date dateDernierMessage) {
		super(uniqueId, false);
		setTitre(titre);
		setNbMessagesNonLus(nbMessagesNonLus);
		setDateDernierMessage(dateDernierMessage);
		this.messages = new TreeSet<>();
	}
	
	/**
	 * Crée un ticket complet
	 * @param uniqueId Identifiant unique
	 * @param titre Titre du ticket
	 * @param messages Ensemble des messages
	 * @param dateCreation Date de création du ticket
	 */
	public Ticket(int uniqueId, String titre, NavigableSet<Message> messages, 
			Date dateCreation, Date dateDernierMessage) {
		super(uniqueId, true);
		setTitre(titre);
		this.messages = messages;
		setDateCreation(dateCreation);
		setDateDernierMessage(dateDernierMessage);
	}
	
	
	
	

	/**
	 * Obtenir le titre
	 * @return titre
	 */
	public String getTitre() throws LacunaireException {
		if (!estComplet())
			throw new LacunaireException(this, "getTitre");
		return titre;
	}

	/**
	 * Définir le titre
	 * @param titre Le titre à définir
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * Obtenir l'ensemble des messages
	 * @return messages
	 */
	public NavigableSet<Message> getMessages() {
		if (!estComplet())
			throw new LacunaireException(this, "getMessage");
		return messages;
	}

	/**
	 * Ajouter un message
	 * @param message Le message à ajouter
	 */
	public void addMessage(Message message) {
		this.messages.add(message);
	}
	
	/**
	 * Ajouter des messages
	 * @param messages
	 */
	public void addMessages(Message...messages) {
		for(Message m: messages)
			this.messages.add(m);
	}

	/**
	 * Ajouter des messages
	 * @param messages
	 */
	public void addMessages(Collection<Message> messages) {
		this.messages.addAll(messages);
	}
	
	
	/**
	 * Met a jour la variable privée nbMessagesNonLus
	 * Le ticket DOIT être complet
	 */
	private void updateNbMessagesNonLus() {
		// Java 8
		// nbMessagesNonLus = (int) messages.stream().filter(m -> m.getStatutUtilisateur()!=StatutDeLecture.LU).count();
		int nbNonLus = 0;
		for (Message m: messages)
			if (m.getStatutUtilisateur()!=StatutDeLecture.LU)
				++nbNonLus;
		nbMessagesNonLus = nbNonLus;
	}

	/**
	 * Obtenir le nombre de messages non lus
	 * @return Le nombre de messages non lus
	 *   estComplet -> nombre de messages stockés non lus
	 *  !estComplet -> nombre transmis lors de l'instanciation
	 */
	public int getNbMessagesNonLus() {
		if (estComplet())
			updateNbMessagesNonLus();
		return nbMessagesNonLus;
	}

	/**
	 * Définir le nbMessagesNonLus
	 * @param nbMessagesNonLus Le nbMessagesNonLus à définir
	 */
	public void setNbMessagesNonLus(int nbMessagesNonLus) {
		this.nbMessagesNonLus = nbMessagesNonLus;
	}

	/**
	 * Obtenir la dateCreation
	 * @return dateCreation
	 */
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * Définir la dateCreation
	 * @param dateCreation Le dateCreation à définir
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	
	private void updateDateDernierMessage() {
		NavigableSet<Message> set = new TreeSet<>();
		set.addAll(messages);
		dateDernierMessage = set.last().getDateEmission();
	}

	/**
	 * Obtenir la dateDernierMessage
	 * @return dateDernierMessage
	 */
	public Date getDateDernierMessage() {
		if (estComplet())
			updateDateDernierMessage();
		return dateDernierMessage;
	}

	/**
	 * Définir la dateDernierMessage
	 * @param dateDernierMessage Le dateDernierMessage à définir
	 */
	public void setDateDernierMessage(Date dateDernierMessage) {
		this.dateDernierMessage = dateDernierMessage;
	}

	@Override
	public int compareTo(Ticket autre) {
		int cmp = dateDernierMessage.compareTo(autre.getDateDernierMessage());
		if (cmp == 0)
			cmp = getIdentifiantUnique() - autre.getIdentifiantUnique();
		return cmp;
	}
	
	// equals dans AbstractIdentifiable
	
	@Override
	public String toString() {
		return super.toString(
				"titre=" + titre, 
				"non_lus=" + nbMessagesNonLus
				);
	}

}
