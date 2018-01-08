/**
 * Fichier Instance.java
 * @date 3 janv. 2018
 * @author Quentin MARTY
 *         quentin.marty@univ-tlse3.fr
 */
package serveur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import modele.Groupe;
import modele.KeyIdentifiable;
import modele.Message;
import modele.StatutDeLecture;
import modele.Ticket;
import modele.Utilisateur;

public class Instance implements IInstance {
	
	static final String JDBC_DRIVER = "mysql.src.com.mysql.jdbc.Driver";  
	
	
	// DB distante
	static final String DB_URL_DISTANT = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11214606";
	static final String USER_DISTANT = "sql11214606";
	static final String PASS_DISTANT = "Qrm3V4MXQG";
	
	
	// DB locale
	static final String DB_URL_LOCAL = "jdbc:mysql://localhost:3306/projetS5";
	static final String USER_LOCAL = "s5";
	static final String PASS_LOCAL = "s5";
	
	// DB retenue à l'instanciation
	private static String DB_URL;
	private static String USER;
	private static String PASS;
	
	
	

	private Connection conn;
	
	
	
	
	
	public Instance(boolean local){
		
		if (local){
			DB_URL = DB_URL_LOCAL;
			USER = USER_LOCAL;
			PASS = PASS_LOCAL;
		}
		else {
			DB_URL = DB_URL_DISTANT;
			USER = USER_DISTANT;
			PASS = PASS_DISTANT;
		}
		
		// Connexion à la db : une seule fois à l'instanciation ?
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			System.out.println("Local: " + local);
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
		}
		catch (ClassNotFoundException e){
			System.err.println("Impossible de charger le driver mysql");
			System.err.println("Placez mysql-connector-java-5.1.44-bin.jar dans le même dossier que cet exécutable");
			System.exit(1);
		}
		catch (SQLException e){
			System.err.println("Impossible de se connecter à la base de données");
			e.printStackTrace();
			System.exit(2);
		}
		
		System.out.println("Connexion avec la basse de données réussie");
	}
	
	
	
	
	
	
	
	/* FONCTIONS PUREMENT SQL */
	
	/**
	 * Vérifie un couple d'identifiants pour la connexion, renvoie l'id de l'utilisateur ou -1
	 * @param nom
	 * @param pass
	 * @return -1 si connexion refusée, l'identifiant numérique de l'utilisateur si connexion acceptée
	 * @throws SQLException
	 */
	public int sqlConnexion(String nom, String pass) throws SQLException {
		return 1;
	}
	
	/**
	 * Vérifie un couple d'identifiants pour la connexion ADMIN, renvoie l'id de l'utilisateur ou -1
	 * @param nom
	 * @param pass
	 * @return -1 si connexion refusée, l'identifiant numérique de l'utilisateur si connexion acceptée
	 * @throws SQLException
	 */
	public int sqlConnexionAdmin(String nom, String pass) throws SQLException {
		return 1;
	}
	
	/**
	 * Construit un utilisateur d'après son id numérique
	 * @param id Identifiant de l'utilisateur
	 * @return Utilisateur
	 * @throws SQLException
	 */
	public Utilisateur sqlSelectUtilisateur(int id) throws SQLException {	
		
		Utilisateur u;
		String sql = "SELECT * FROM user WHERE id_user ="+ id +" LIMIT 1";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		// à mettre en getInt si on décide de changer le constructeur d'utilisateur
		u = new Utilisateur(rs.getString("id_user"), rs.getString("nom_user"), rs.getString("prenom_user"));
	
		return u;
	}
	
	/**
	 * Select de tous les utilisateurs
	 * @return Set
	 * @throws SQLException
	 */
	public Set<Utilisateur> sqlSelectUtilisateurs() throws SQLException {	
		Set<Utilisateur> retourne = new TreeSet<>();
		String sql = "SELECT id_user, nom_user, prenom_user FROM user";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			Utilisateur u = new Utilisateur(rs.getString("id_user"), rs.getString("nom_user"), rs.getString("prenom_user"));	
			retourne.add(u);
		}
		return retourne;
	}
	
	/**
	 * Construit un message d'après son id après avoir vérifié que l'utilisateur peut le consulter
	 * @param idMsg Id du message
	 * @return Le message demandé, ou null
	 * @throws SQLException
	 */
	public Message sqlSelectMessage(int idMsg, int idUser) throws SQLException {
		
		//TODO sql peut consulter le message ?
		boolean messageVisible = true;
		
		if (!messageVisible) 
			return null;
		
		return sqlSelectMessage(idMsg);
		
	}
	
	/**
	 * Construit un message d'après son id
	 * @param idMsg Id du message
	 * @param idUser Id de l'utilisateur faisant la requête
	 * @return Le message demandé, ou null
	 * @throws SQLException
	 */
	public Message sqlSelectMessage(int idMsg) throws SQLException {
		
		String sql = "SELECT * FROM message WHERE id_message ="+ idMsg +" LIMIT 1";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Utilisateur u = new Utilisateur(rs.getString("id_user"),"nom","prenom");
		Message retourne = new Message(rs.getInt("id_message"), u, rs.getString("contenu"), rs.getDate("date_message"), null);
		return retourne;
	}
	
	
	/**
	 * Construit l'ensemble de tous les messages
	 * @return
	 * @throws SQLException
	 */
	public Set<Message> sqlSelectMessages() throws SQLException {
		Set<Message> retourne = new TreeSet<>();
		String sql = "SELECT * FROM message";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			Utilisateur u = new Utilisateur(rs.getString("id_user"),"nom","prenom");
			Message mess = new Message(rs.getInt("id_message"), u, rs.getString("contenu"), rs.getDate("date_message"), null);	
			retourne.add(mess);
		}
		return retourne;
	}
	
	/**
	 * Construit un ticket incomplet (avec uniquement le nombre de messages non lus, et la date du dernier)
	 * @param idTicket Id du ticket
	 * @param idUser Id de l'utilisateur faisant la demande
	 * @return Le ticket, ou null
	 * @throws SQLException
	 */
	public Ticket sqlSelectTicketIncomplet(int idTicket, int idUser) throws SQLException {
		return null;
	}
	
	/**
	 * Construit un ticket complet (avec tous les messages)
	 * @param idTicket Id du ticket
	 * @param idUser Id de l'utilisateur faisant la demande
	 * @return Le ticket, ou null
	 * @throws SQLException
	 */
	
	public Ticket sqlSelectTicket(int idTicket, int idUser) throws SQLException {

		//TODO SQL : ticket visible par le client ?
		boolean ticketVisibleParLeClient = true;
		
		if (!ticketVisibleParLeClient)
			return null;
		
		return sqlSelectTicket(idTicket);
	}

	@Override
	public Ticket sqlSelectTicket(int idTicket) throws SQLException {

		Ticket t;
		
		
		Statement stmt = null;
		System.out.println(" ** Select ticket ** ");
		stmt = conn.createStatement();
		String sql = "SELECT * FROM ticket WHERE id_ticket = " + idTicket + " LIMIT 1";
		ResultSet rs = stmt.executeQuery(sql);
		
		if(rs.next()){
			Date dateCreationTicket = rs.getDate("creation_ticket");
			String titreTicket = rs.getString("titre_ticket");
			int id_groupe_parent = rs.getInt("id_groupe");
			NavigableSet<Message> messages = new TreeSet<Message>();

			String sql2 = "SELECT id_message,contenu,user.id_user,nom_user,prenom_user,date_message FROM message, user WHERE message.id_user = user.id_user AND id_ticket = " + idTicket + " ORDER BY date_message DESC";

			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql2);

			while(rs2.next()){
				String contenuMsg = rs2.getString("contenu");
				Utilisateur emetteur = new Utilisateur(rs2.getString("id_user"), rs2.getString("nom_user"), rs2.getString("prenom_user"));
				Date dateMsg = rs2.getDate("date_message");
				
				//TODO SQL Calcul des statuts de lecture pour les lecteurs du message
				NavigableMap<Utilisateur, StatutDeLecture> statuts = new TreeMap<>();;

				// Instanciation du message
				Message unMessage = new Message(0, emetteur, contenuMsg, dateMsg, statuts);
				unMessage.setParent(new KeyIdentifiable(idTicket));
				messages.add(unMessage);
			}
			rs2.close();
			
			// On crée le ticket
			t = new Ticket(idTicket, titreTicket, messages, dateCreationTicket);

			// On renseigne son parent
			t.setParent(new KeyIdentifiable(id_groupe_parent));
			
		}
		else {
			t = null;
		}
		
		rs.close();
		stmt.close();
	
		
		return t;
	}
	
	
	
	public Set<Ticket> sqlSelectTickets() throws SQLException {

		Set<Ticket> retourne = new TreeSet<>();
		String sql = "SELECT * FROM ticket";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		
		while(rs.next()) {
			// Recuperation des messages pour chaque ticket (juste l'id suffit pour cette méthode)
			NavigableSet<Message> messages = new TreeSet<Message>();
			String sql2 = "SELECT id_message FROM message WHERE id_ticket = " + rs.getShort("id_ticket") + " LIMIT 1";
	
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql2);
	
			while(rs2.next()){
				Message unMessage = new Message(rs2.getInt("id_message"), null, null, null, null);
				messages.add(unMessage);
			}
			Ticket t = new Ticket(rs.getShort("id_ticket"), rs.getString("titre_ticket"), messages, rs.getDate("creation_ticket"));
			retourne.add(t);
		}

		return retourne;
	}
	
	
	
	/**
	 * Construit un groupe incomplet (contenant des tickets incomplets)
	 * @param idGroupe Id du groupe
	 * @param nomGroupe Nom du groupe
	 * @param idUser Id de l'utilisateur faisant la requête
	 * @return Le groupe, ou null
	 * @throws SQLException
	 */
	public Groupe sqlSelectGroupe(int idGroupe, String nomGroupe, int idUser) throws SQLException {
		return null;
	}
	
	/**
	 * Construit la liste de tous les groupes (incomplets)
	 * @param idUser Id de l'utilisateur faisant la requête
	 * @return
	 * @throws SQLException
	 */
	public NavigableSet<Groupe> sqlSelectGroupes(int idUser) throws SQLException {
		NavigableSet<Groupe> retourne = new TreeSet<>();
		Statement stmt = null;
		
		System.out.println(" ** Select groupes ** ");
		stmt = conn.createStatement();
		String sql = "SELECT id_groupe, nom_groupe FROM groupe";
		System.out.println("Execute query...");
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()){
			
			int id_groupe = rs.getInt("id_groupe");
			String nom_groupe = rs.getString("nom_groupe");
			
			// Plus simple de créer avec le constructeur (et pareil tout le long)
			Groupe unGroupe = new Groupe(id_groupe, nom_groupe);


			// On ajoute les tickets si l'utilisateur appartient au groupe
			// ou s'il a créé le ticket.
			// On aurait dû garder le champ 'créateur_ticket', ç'aurait été plus simple :-)
			// Mais c'est aussi l'émetteur du premier message
			//TODO SQL :  if est_dans_groupe(utilisateur) or nb_messages_dans_ticket(utilisateur) > 0
			boolean groupeVisibleParLeClient = true;
			
			if (groupeVisibleParLeClient){
			
				// Recuperation des tickets pour le groupe
				// On renvoie des tickets incomplets (sans les messages)
				String sql2 = "SELECT * FROM ticket WHERE id_groupe = " + id_groupe;
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sql2);
				while(rs2.next()){
					int id_ticket = rs2.getInt("id_ticket");
					String titreTicket = rs2.getString("titre_ticket");
					
					//TODO SQL : nombre de messages non lus, date dernier message
					int nb_msg_non_lus = 0;
					Date date_dernier_message = new Date();

					Ticket unTicket = new Ticket(id_ticket,titreTicket, nb_msg_non_lus, date_dernier_message);
	
					unGroupe.addTicketConnu(unTicket);
				}
				stmt2.close();
				rs2.close();
			}
			retourne.add(unGroupe);
		}		
		rs.close();
		stmt.close();
		
		return retourne;
	}












	@Override
	public int sqlInsertGroupe(String nom) throws SQLException {

	    Statement statement = conn.createStatement();
	    String query;
	   	query = "INSERT INTO groupe (id_groupe, nom_groupe) VALUES (NULL, "+ nom +")";
	  	statement.executeUpdate(query);	
		
		//TODO récupérer l'id
		return 0;
	}







	@Override
	public int sqlInsertUtilisateur(String nom, String prenom, String nickname,
			String pass) throws SQLException {
	    Statement statement = conn.createStatement();
		String query = "INSERT INTO user (id_user, password_user, nickname_user, nom_user, prenom_user) "
				+ "VALUES (NULL, NULL,'"+nickname+"', '"+nom+"', "+prenom+")";
		statement.executeUpdate(query);
		
		//TODO récupérer l'id
		return 0;
	}







	@Override
	public int sqlInsertTicket(String titre, String premierMessage, int idUser, int idGroupe)
			throws SQLException {
		
		Statement statement = null;
		System.out.println(" ** insert ticket ** ");
	    java.sql.Date dateCurrent = new java.sql.Date(new Date().getTime());

	    statement = conn.createStatement();
		String query = "INSERT INTO ticket (id_ticket, titre_ticket, creation_ticket, id_groupe) VALUES (NULL, '"+titre+"', '"+dateCurrent+"', "+idGroupe+")";
		
		// On demande de renvoyer les clés générées
		statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

		ResultSet rs = statement.getGeneratedKeys();
		int id_ticket = 0;
		if (rs.next()) {
			id_ticket = rs.getInt(1);
			
			// sqlInsertMessage() ?
		
	
			String query2 = "INSERT INTO message (id_message, id_ticket, id_user, contenu, date_message) VALUES (NULL, ?, ?, ?, ?)";
	
			PreparedStatement preparedStmt = conn.prepareStatement(query2);
			preparedStmt.setInt (1, id_ticket);
			preparedStmt.setInt (2, idUser);
			preparedStmt.setString (3, premierMessage);
			preparedStmt.setDate (4, dateCurrent);
			preparedStmt.execute();
	
			
		}

		return id_ticket;
	}







	@Override
	public int sqlInsertMessage(String contenu, int idUser, int idTicket)
			throws SQLException {	
		java.sql.Date dateCurrent = new java.sql.Date(new Date().getTime());
		String query = "INSERT INTO message (id_message, id_ticket, id_user, contenu, date_message) VALUES (NULL, ?, ?, ?, ?)";
		
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		preparedStmt.setInt (1, idTicket);
		preparedStmt.setInt(2, idUser);
		preparedStmt.setString (3, contenu);
		preparedStmt.setDate (4, dateCurrent);
		preparedStmt.execute();
		
		ResultSet rs = preparedStmt.getGeneratedKeys();
		int idMsg = 0;
		if (rs.next())
			idMsg = rs.getInt(1);
		
		return idMsg;
		
/*
 * Fait le même travail ?
		@Override // manque l'id du ticket concerné (manque trop d'infos ou alors j'ai pas compris ce qu'elle doit faire)
		public void adminSetMessage(ComAdresse admin, Message message) {
		    java.sql.Date dateCurrent = new java.sql.Date(new Date().getTime());
		    Statement statement = conn.createStatement();
			String query = "INSERT INTO user (id_message, id_ticket, id_user, contenu, date_message) "
					+ "VALUES (NULL, NULL, NULL, '"+message.getTexte()+"', "+dateCurrent+")";
			statement.executeUpdate(query);
		}
*/		
	}







	@Override
	public int sqlUpdateGroupe(int id, String nom) throws SQLException {
	    Statement statement = conn.createStatement();
	    String query;
    	query = "UPDATE groupe SET nom_groupe = \""+ nom +"\" WHERE id_groupe = "+ id +" LIMIT 1";
		statement.executeUpdate(query);	
		
		return id;
	}







	@Override
	public int sqlUpdateUtilisateur(int id, String nom, String prenom,
			String nickname, String pass) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int sqlUpdateTicket(int idTicket, String titre, int idUser,
			int idGroupe) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int updateMessage(int idMsg, String contenu, int idUser,
			int idTicket) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void sqlRejoindreGroupe(int idUser, int idGroupe)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void sqlQuitterGroupe(int idUser, int idGroupe) throws SQLException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void deleteGroupe(int id) throws SQLException {
	    Statement statement = conn.createStatement();
		String query = "DELETE FROM groupe WHERE id_groupee = "+id+" LIMIT 1";
		statement.executeUpdate(query);
	}



	@Override
	public void deleteTicket(int id) throws SQLException {
	    Statement statement = conn.createStatement();
		String query = "DELETE FROM ticket WHERE id_ticket = "+id+" LIMIT 1";
		statement.executeUpdate(query);
	}



	@Override
	public void deleteMessage(int id) throws SQLException {
	    Statement statement = conn.createStatement();
		String query = "DELETE FROM message WHERE id_message = "+id+" LIMIT 1";
		statement.executeUpdate(query);
	}


	@Override
	public void deleteUtilisateur(int id) throws SQLException {
	    Statement statement = conn.createStatement();
		String query = "DELETE FROM user WHERE id_user = "+id+" LIMIT 1";
		statement.executeUpdate(query);
	}
	
	
	
	
	
	
}

