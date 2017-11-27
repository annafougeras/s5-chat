/**
 * Fichier Groupe.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package modele;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * 
 */
public class Groupe extends AbstractIdentifiable implements Comparable<Groupe> {

	private String nom;
	private NavigableSet<Ticket> ticketsConnus;

	/**
	 * Constructeur de groupe
	 * @param uniqueId Identifiant unique
	 * @param nom Nom du groupe
	 * @param ticketsConnus Tickets du groupe connus par l'utilisateur
	 */
	public Groupe(int uniqueId, String nom, NavigableSet<Ticket> ticketsConnus) {
		super(uniqueId);
		setNom(nom);
		addTicketsConnus(ticketsConnus);
	}
	
	/**
	 * Constructeur de groupe sans tickets connus
	 * @param uniqueId Identifiant unique
	 * @param nom Nom du groupe
	 */
	public Groupe(int uniqueId, String nom) {
		super(uniqueId);
		setNom(nom);
		ticketsConnus = new TreeSet<Ticket>();
	}
	
	
	
	
	
	
	/**
	 * Obtenir le nom
	 * @return nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Définir le nom
	 * @param nom Le nom à définir
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Obtenir les tickets connus
	 * @return ticketsConnus
	 */
	public NavigableSet<Ticket> getTicketsConnus() {
		return ticketsConnus;
	}

	/**
	 * Ajouter un ticket connu
	 * @param ticket Le ticket à ajouter
	 */
	public void addTicketConnu(Ticket ticket) {
		ticketsConnus.add(ticket);
	}
	
	/**
	 * Ajouter des tickets connus
	 * @param tickets Les tickets à ajouter
	 */
	public void addTicketsConnus(Ticket...tickets) {
		for (Ticket t: tickets)
			addTicketConnu(t);
	}

	/**
	 * Ajouter des tickets connus
	 * @param tickets Les tickets à ajouter
	 */
	public void addTicketsConnus(Collection<Ticket> tickets) {
		ticketsConnus.addAll(tickets);
	}

	
	
	
	
	@Override
	public int compareTo(Groupe arg0) {
		int cmp = getNom().compareTo(arg0.getNom());
		if (cmp == 0)
			cmp = getIdentifiantUnique() - arg0.getIdentifiantUnique();
		return cmp;
	}
	
	// equals dans AbstractIdentifiable
	
	@Override
	public int hashCode() {
		return 47 * getNom().hashCode() * getIdentifiantUnique();
	}
	
	
	@Override
	public String toString() {
		return super.toString("nom="+nom);
	}
	

}
