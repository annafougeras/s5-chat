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
	
	private static final long serialVersionUID = -288563539327011854L;
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
			Date dateCreation) {
		super(uniqueId, true);
		setTitre(titre);
		this.messages = messages;
		setDateCreation(dateCreation);
		updateDateDernierMessage();
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
	public void updateNbMessagesNonLus(Utilisateur user) {
		if (!estComplet())
			throw new LacunaireException("updatNbMessageNonLus doit être appelé sur un ticket complet");
		
		int nbNonLus = 0;
		for (Message m: messages)
			if (m.getStatuts().get(user)!=StatutDeLecture.LU)
				++nbNonLus;
		nbMessagesNonLus = nbNonLus;
	}

	/**
	 * Obtenir le nombre de messages non lus
	 * Ce nombre peut être recalculé avec updateNbMessagesNonLus, 
	 * si le ticket n'est pas incomplet
	 * 
	 * Note : cette valeure concerne les message non lus d'un utilisateur 
	 * (et non pas de tout le groupe). Est-ce ce qu'on souhaite ?
	 * @return Le nombre de messages non lus
	 */
	public int getNbMessagesNonLus() {
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
		if (messages.size() == 0)
			dateDernierMessage = dateCreation;
		else
			dateDernierMessage = messages.last().getDateEmission();
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
			cmp = new IdentifiableComparator().compare(this, autre);
		return cmp;
	}
	
	// equals dans AbstractIdentifiable
	
	@Override
	public String toString() {
		return "(" + nbMessagesNonLus + ") " + titre;
	}

}
