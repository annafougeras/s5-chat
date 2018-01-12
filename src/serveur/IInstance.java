/**
 * Fichier IInstance.java
 * @date 8 janv. 2018
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package serveur;

import java.sql.SQLException;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;

import modele.Groupe;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

/**
 * 
 */
public interface IInstance {

	
	/**
	 * Vérifie un couple d'identifiants pour la connexion, renvoie l'id de l'utilisateur ou -1
	 * @param nom
	 * @param pass
	 * @return -1 si connexion refusée, l'identifiant numérique de l'utilisateur si connexion acceptée
	 * @throws SQLException
	 */
	public int sqlConnexion(String nom, String pass) throws SQLException;
	
	/**
	 * Vérifie un couple d'identifiants pour la connexion ADMIN, renvoie l'id de l'utilisateur ou -1
	 * @param nom
	 * @param pass
	 * @return -1 si connexion refusée, l'identifiant numérique de l'utilisateur si connexion acceptée
	 * @throws SQLException
	 */
	public int sqlConnexionAdmin(String pass) throws SQLException;
	
	/**
	 * Construit un utilisateur d'après son id numérique
	 * @param id Identifiant de l'utilisateur
	 * @return Utilisateur
	 * @throws SQLException
	 */
	public Utilisateur sqlSelectUtilisateur(int id) throws SQLException;
	
	/**
	 * Select de tous les utilisateurs
	 * @return Set
	 * @throws SQLException
	 */
	public Set<Utilisateur> sqlSelectUtilisateurs() throws SQLException;
	
	/**
	 * Construit un message d'après son id après avoir vérifié que l'utilisateur peut le consulter
	 * @param idMsg Id du message
	 * @return Le message demandé, ou null
	 * @throws SQLException
	 */
	public Message sqlSelectMessage(int idMsg, int idUser) throws SQLException;
	
	/**
	 * Construit un message d'après son id
	 * @param idMsg Id du message
	 * @param idUser Id de l'utilisateur faisant la requête
	 * @return Le message demandé, ou null
	 * @throws SQLException
	 */
	public Message sqlSelectMessage(int idMsg) throws SQLException;
	
	
	/**
	 * Construit l'ensemble de tous les messages
	 * @return
	 * @throws SQLException
	 */
	public Set<Message> sqlSelectMessages() throws SQLException;
	
	/**
	 * Construit un ticket incomplet (avec uniquement le nombre de messages non lus, et la date du dernier)
	 * @param idTicket Id du ticket
	 * @param idUser Id de l'utilisateur faisant la demande
	 * @return Le ticket, ou null
	 * @throws SQLException
	 */
	//Ticket sqlSelectTicketIncomplet(int idTicket, int idUser) throws SQLException;
	
	/**
	 * Construit un ticket complet (avec tous les messages)
	 * @param idTicket Id du ticket
	 * @param idUser Id de l'utilisateur faisant la demande
	 * @return Le ticket, ou null
	 * @throws SQLException
	 */
	public Ticket sqlSelectTicket(int idTicket) throws SQLException;
	
	/**
	 * Construit un ticket complet (avec tous les messages) après avoir vérifié que l'utilisateur peut le consulter
	 * @param idTicket Id du ticket
	 * @param idUser Id de l'utilisateur faisant la demande
	 * @return Le ticket, ou null
	 * @throws SQLException
	 */
	
	public Ticket sqlSelectTicket(int idTicket, int idUser) throws SQLException;

	public Set<Ticket> sqlSelectTickets() throws SQLException;
	
	/**
	 * Construit un groupe incomplet (contenant des tickets incomplets)
	 * @param idGroupe Id du groupe
	 * @param idUser Id de l'utilisateur faisant la requête (ou -1 = ok)
	 * @return Le groupe, ou null
	 * @throws SQLException
	 */
	public Groupe sqlSelectGroupe(int idGroupe, int idUser) throws SQLException;
	
	/**
	 * Construit la liste de tous les groupes (incomplets)
	 * @param idUser Id de l'utilisateur faisant la requête
	 * @return
	 * @throws SQLException
	 */
	public NavigableSet<Groupe> sqlSelectGroupes(int idUser) throws SQLException;
	
	public TreeMap<Groupe,NavigableSet<Utilisateur>> sqlSelectUtilisateursParGroupe() throws SQLException;
	
	

	
	public int sqlInsertGroupe(String nom) throws SQLException;
	
	public int sqlInsertUtilisateur(String nom, String prenom, String nickname, String pass) throws SQLException;
	
	public int sqlInsertTicket(String titre, String premierMessage, int idUser, int idGroupe) throws SQLException;
	
	public int sqlInsertMessage(String contenu, int idUser, int idTicket) throws SQLException;
	
	
	public int sqlUpdateGroupe(int id, String nom) throws SQLException;
	
	public int sqlUpdateUtilisateur(int id, String nom, String prenom, String nickname, String pass) throws SQLException;

	public int sqlUpdateTicket(int idTicket, String titre, int idGroupe) throws SQLException;
	
	public int updateMessage(int idMsg, String contenu, int idUser, int idTicket) throws SQLException;
	
	//TODO renommer en sqlDeleteGroupe
	public void deleteGroupe(int id) throws SQLException;

	//TODO renommer en sqlDeleteTicket
	public void deleteTicket(int id) throws SQLException;

	//TODO renommer en sqlDeleteMessage
	public void deleteMessage(int id) throws SQLException;

	//TODO renommer en sqlDeleteUtilisateur
	public void deleteUtilisateur(int id) throws SQLException;
	
	
	public void sqlRejoindreGroupe(int idUser, int idGroupe) throws SQLException;
	
	public void sqlQuitterGroupe(int idUser, int idGroupe) throws SQLException;

	
	
	public void sqlSetStatut(int idUser, int idTicket, int statut) throws SQLException;
	
	
	
}
