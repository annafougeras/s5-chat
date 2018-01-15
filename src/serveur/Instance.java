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
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			System.out.println("Local: " + local);
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
		}
		catch (ClassNotFoundException e){
			System.err.println("Impossible de charger le driver mysql");
			System.err.println("Placez mysql-connector-java-5.1.44-bin.jar dans le meme dossier que cet executable");
			System.exit(1);
		}
		catch (SQLException e){
			System.err.println("Impossible de se connecter à la base de donnees");
			e.printStackTrace();
			System.exit(2);
		}
		
		System.out.println("Connexion avec la base de donnees reussie");
	}
	
	
	
	
	/**
	 * Tous les identifiants des utilisateurs pouvant consulter un ticket
	 * @param idTicket
	 * @return
	 * @throws SQLException
	 */
	private Set<Integer> utilisateursPouvantConsulterUnTicket(int idTicket) throws SQLException {
		Set<Integer> set = new TreeSet<>();

		Statement stmt1 = conn.createStatement();
		Statement stmt2 = conn.createStatement();
		
		// membres du groupe
		String sql1 = "select id_user FROM ticket, appartenance WHERE appartenance.id_groupe = ticket.id_groupe AND ticket.id_ticket = "+ idTicket;
		ResultSet rs1 = stmt1.executeQuery(sql1);
		System.out.println(sql1);
		while(rs1.next())
			set.add(rs1.getInt("id_user"));

		// emetteur du ticket
		String sql2 = "select id_user FROM ticket, message WHERE message.id_ticket = ticket.id_ticket AND ticket.id_ticket = "+ idTicket +" LIMIT 1";
		ResultSet rs2= stmt2.executeQuery(sql2);
		System.out.println(sql2);
		while(rs2.next())
			set.add(rs2.getInt("id_user"));
		
		return set;
	}
	
	
	
	/**
	 * Determine si un ticket est consultable par un utilisateur
	 * @param idTicket
	 * @param idUser
	 * @return
	 * @throws SQLException
	 */
	public boolean ticketConsultable(int idTicket, int idUser) throws SQLException {
		return utilisateursPouvantConsulterUnTicket(idTicket).contains(new Integer(idUser));
	}


	/**
	 * Met tous les statuts de lecture d'un message a 'envoye'
	 * @param idMsg
	 * @throws SQLException
	 */
	private void initialiseStatutsDeLecture(int idMsg) throws SQLException {
		Statement stmt1 = conn.createStatement();
		Statement stmt2 = conn.createStatement();
		String sql1;
		String sql2;
		
		sql1 = "SELECT id_ticket FROM message WHERE id_message = " + idMsg;
		ResultSet rs1= stmt1.executeQuery(sql1);
		rs1.next();
	
		for (int idUser: utilisateursPouvantConsulterUnTicket(rs1.getInt("id_ticket"))){
			sql2 = "INSERT INTO statut VALUES ("+idMsg+", "+idUser+", '"+StatutDeLecture.ENVOYE.toInt()+"')";
			stmt2.executeUpdate(sql2);		
		}
		
	}


	/**
	 * Donne les statuts de lecture d'un message
	 * @param idMessage
	 * @return
	 * @throws SQLException
	 */
	private NavigableMap<Utilisateur,StatutDeLecture> statutsDeLecture(int idMessage) throws SQLException {
		NavigableMap<Utilisateur,StatutDeLecture> statuts = new TreeMap<>();

		Statement stmt1 = conn.createStatement();
		ResultSet rs1= stmt1.executeQuery("SELECT id_user,statut FROM statut WHERE id_message = " + idMessage);
		while (rs1.next()) {
			int idUser = rs1.getInt("id_user");
			int statut = rs1.getInt("statut");
			
			Utilisateur u = sqlSelectUtilisateur(idUser);
			StatutDeLecture s = StatutDeLecture.fromInt(statut);
			statuts.put(u, s);
		}
		return statuts;
	}
	
	
	/**
	 * Donne le nombre de messages non lus par un utilisateur sur un ticket
	 * @param idTicket
	 * @param idUser
	 * @return
	 * @throws SQLException
	 */
	private int nbMessagesNonLus(int idTicket, int idUser) throws SQLException {
		Statement stmt1 = conn.createStatement();
		ResultSet rs1= stmt1.executeQuery("select count(*) as nb FROM statut,message WHERE statut.statut <= 1 AND statut.id_user = "+idUser+" AND statut.id_message = message.id_message AND message.id_ticket = "+idTicket);
		rs1.next();
		return rs1.getInt("nb");
	}
	



	/* Met a� jour le statut d'un utilisateur pour tous les messages du ticket */
	@Override
	public void sqlSetStatut(int idUser, int idTicket, int statut) throws SQLException {
	    Statement statement = conn.createStatement();
	    String query = "UPDATE statut, message SET statut.statut = '"+ statut +"' WHERE statut.id_user = "+ idUser +" AND statut.id_message = message.id_message AND message.id_ticket = " + idTicket;
		statement.executeUpdate(query);	
	}



	
	
	
	
	
	
	
	
	
	
	/**
	 * Verifie un couple d'identifiants pour la connexion, renvoie l'id de l'utilisateur ou -1
	 * @param nom
	 * @param pass
	 * @return -1 si connexion refusee, l'identifiant numerique de l'utilisateur si connexion acceptee
	 * @throws SQLException
	 */
	public int sqlConnexion(String nom, String pass) throws SQLException {
		String sql = "SELECT count(*) as connexion, id_user FROM user WHERE nickname_user ='"+ nom +"' AND password_user = '"+ Sha256.sha256("qt"+pass+"pi") +"' LIMIT 1";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		if (rs.next())
			if(rs.getInt("connexion") == 1)
				return rs.getInt("id_user");
		return -1;
	}
	
	/**
	 * Verifie un couple d'identifiants pour la connexion ADMIN, renvoie l'id de l'utilisateur ou -1
	 * @param nom
	 * @param pass
	 * @return -1 si connexion refusee, l'identifiant numerique de l'utilisateur si connexion acceptee
	 * @throws SQLException
	 */
	public int sqlConnexionAdmin(String pass) throws SQLException {
		// Connexion admin toujours ok
		return 1;
	}
	
	/**
	 * Construit un utilisateur d'apres son id numerique
	 * @param id Identifiant de l'utilisateur
	 * @return Utilisateur
	 * @throws SQLException
	 */
	public Utilisateur sqlSelectUtilisateur(int id) throws SQLException {	
		Utilisateur u = null;
		String sql = "SELECT * FROM user WHERE id_user ="+ id +" LIMIT 1";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		if (rs.next())
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



	@Override
	public TreeMap<Groupe, NavigableSet<Utilisateur>> sqlSelectUtilisateursParGroupe() throws SQLException {
		
		TreeMap<Groupe, NavigableSet<Utilisateur>> map = new TreeMap<>();
		
		String sql1 = "SELECT * FROM groupe";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(sql1);
		
		while (rs1.next()) {
			int idGroupe = rs1.getInt("id_groupe");
			String nomGroupe = rs1.getString("nom_groupe");
			Groupe groupe = new Groupe(idGroupe, nomGroupe);
			TreeSet<Utilisateur> set = new TreeSet<>();
			map.put(groupe, set);
			
			String sql2 = "SELECT id_user FROM appartenance WHERE id_groupe = " + idGroupe;
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql2);
			while (rs2.next()) {
				int idUser = rs2.getInt("id_user");
				Utilisateur u = sqlSelectUtilisateur(idUser);
				set.add(u);
			}
			stmt2.close();
			rs2.close();
		}
		stmt1.close();
		rs1.close();
		
		return map;
		
	}

	
	
	
	/**
	 * Construit un message d'après son id après avoir vérifié que l'utilisateur peut le consulter
	 * @param idMsg Id du message
	 * @return Le message demandé, ou null
	 * @throws SQLException
	 */
	public Message sqlSelectMessage(int idMsg, int idUser) throws SQLException {
		String sql = "SELECT * FROM message WHERE id_message ="+ idMsg +" LIMIT 1";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();

		String sql4 = "SELECT id_groupe FROM ticket WHERE id_ticket = "+ rs.getInt("id_ticket") +" LIMIT 1";
		Statement stmt4 = conn.createStatement();
		ResultSet rs4 = stmt4.executeQuery(sql4);
		rs4.next();

		String sql3 = "SELECT count(*) as valid FROM appartenance WHERE id_user = "+ idUser +" AND id_groupe = "+ rs4.getInt("id_groupe") +" LIMIT 1";
		Statement stmt3 = conn.createStatement();
		ResultSet rs3 = stmt3.executeQuery(sql3);
		rs3.next();

		if(rs3.getInt("valid") > 0) { // si le message peut être lu par l'utilisateur, on construit le message
			String sql2 = "SELECT * FROM user WHERE id_user = "+ rs.getString("id_user") +" LIMIT 1";
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql2);
			rs2.next();
			Utilisateur u = new Utilisateur(rs.getString("id_user"), rs2.getString("nom_user"), rs2.getString("prenom_user"));
			Message retourne = new Message(rs.getInt("id_message"), u, rs.getString("contenu"), rs.getDate("date_message"),
					statutsDeLecture(rs.getInt("id_message")));
			return retourne;
		}
		return null;
	}

	/**
	 * Construit un message d'aprés son id
	 * @param idMsg Id du message
	 * @return Le message demande, ou null
	 * @throws SQLException
	 */
	public Message sqlSelectMessage(int idMsg) throws SQLException {
		String sql = "SELECT * FROM message WHERE id_message ="+ idMsg +" LIMIT 1";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();

		String sql2 = "SELECT * FROM user WHERE id_user = "+ rs.getString("id_user") +" LIMIT 1";
		Statement stmt2 = conn.createStatement();
		ResultSet rs2 = stmt2.executeQuery(sql2);	
		rs2.next();

		Utilisateur u = new Utilisateur(rs.getString("id_user"),rs2.getString("nom_user"),rs2.getString("prenom_user"));
		Message retourne = new Message(rs.getInt("id_message"), u, rs.getString("contenu"), rs.getDate("date_message"), 
				statutsDeLecture(rs.getInt("id_message")));	
		retourne.setParent(new KeyIdentifiable(rs.getInt("id_ticket")));
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
		while(rs.next()){
			String sql2 = "SELECT * FROM user WHERE id_user ="+ rs.getString("id_user") +" LIMIT 1";
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql2);	
			rs2.next();
			Utilisateur u = new Utilisateur(rs.getString("id_user"),rs2.getString("nom_user"),rs2.getString("prenom_user"));
			Message mess = new Message(
					rs.getInt("id_message"), 
					u, 
					rs.getString("contenu"), 
					rs.getDate("date_message"), 
					statutsDeLecture(rs.getInt("id_message")));	
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
		boolean ok = ticketConsultable(idTicket, idUser);
		if (ok) {
			/* UPDATE STATUT RESSOURCE
			 * String sql2 = "SELECT id_message FROM message WHERE id_ticket ="+ idTicket;
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql2);	
			while(rs2.next()) {
				Statement statement = conn.createStatement();
				String query = "UPDATE statut SET statut = 2 WHERE id_message = "+ rs2.getInt("id_message") +" AND id_user = "+ idUser +" LIMIT 1";
				System.out.println(query);
				statement.executeUpdate(query);
			}*/
			return sqlSelectTicket(idTicket);
		}
		else
			return null;
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
				int idMsg = rs2.getInt("id_message");
				String contenuMsg = rs2.getString("contenu");
				Utilisateur emetteur = new Utilisateur(rs2.getString("id_user"), rs2.getString("nom_user"), rs2.getString("prenom_user"));
				Date dateMsg = rs2.getDate("date_message");
				
				NavigableMap<Utilisateur, StatutDeLecture> statuts = statutsDeLecture(idMsg);

				// Instanciation du message
				Message unMessage = new Message(idMsg, emetteur, contenuMsg, dateMsg, statuts);
				unMessage.setParent(new KeyIdentifiable(idTicket));
				messages.add(unMessage);
			}
			rs2.close();
			
			// On cree le ticket
			t = new Ticket(idTicket, titreTicket, messages, dateCreationTicket);
			t.setParent(new KeyIdentifiable(id_groupe_parent));
			
		} else 
			t = null;
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
			// Recuperation des messages pour chaque ticket (juste l'id suffit pour cette methode)
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
	 * @param idUser Id de l'utilisateur faisant la requète (ou -1)
	 * @return Le groupe, ou null
	 * @throws SQLException
	 */
	public Groupe sqlSelectGroupe(int idGroupe, int idUser) throws SQLException {
		System.out.println(" ** Select UN groupe ** ");
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM groupe WHERE id_groupe = "+idGroupe+" LIMIT 1";
		ResultSet rs = stmt.executeQuery(sql);
		Groupe unGroupe = null;		
		if(rs.next()){
			int id_groupe = rs.getInt("id_groupe");
			String nom_groupe = rs.getString("nom_groupe");
			unGroupe = new Groupe(id_groupe, nom_groupe);

			String sql2 = "SELECT id_ticket, titre_ticket FROM ticket WHERE id_groupe = " + idGroupe;
			Statement stmt3 = conn.createStatement();
			ResultSet rs2 = stmt3.executeQuery(sql2);
			while(rs2.next()){
				Ticket unTicket = new Ticket(rs2.getInt("id_ticket"), rs2.getString("titre_ticket"), null, null);
				unGroupe.addTicketConnu(unTicket);
			}
			return unGroupe;
		}
		return null;
	}
	
	/**
	 * Construit la liste de tous les groupes (incomplets)
	 * @param idUser Id de l'utilisateur faisant la requète (ou -1)
	 * @return
	 * @throws SQLException
	 */
	public NavigableSet<Groupe> sqlSelectGroupes(int idUser) throws SQLException {
		NavigableSet<Groupe> retourne = new TreeSet<>();
		Statement stmt = null;
		
		System.out.println(" ** Select groupes ** ");
		stmt = conn.createStatement();
		String sql = "SELECT id_groupe, nom_groupe FROM groupe";
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()){
			
			int id_groupe = rs.getInt("id_groupe");
			String nom_groupe = rs.getString("nom_groupe");
			
			Groupe unGroupe = new Groupe(id_groupe, nom_groupe);

			String sql2 = "SELECT * FROM ticket WHERE id_groupe = " + id_groupe;
			Statement stmt3 = conn.createStatement();
			ResultSet rs2 = stmt3.executeQuery(sql2);
			while(rs2.next()){
				int id_ticket = rs2.getInt("id_ticket");
				
				if (ticketConsultable(id_ticket, idUser)) {
					String titreTicket = rs2.getString("titre_ticket");
					
					int nb_msg_non_lus = nbMessagesNonLus(id_ticket, idUser);
					String sql3 = "SELECT date_message FROM message WHERE id_ticket = " + id_ticket + " ORDER BY date_message DESC LIMIT 1";
					Statement stmt4 = conn.createStatement();
					ResultSet rs3 = stmt4.executeQuery(sql3);
					Date date_dernier_message = new Date();
					if(rs3.next())
						date_dernier_message = rs3.getDate("date_message");

					Ticket unTicket = new Ticket(id_ticket,titreTicket, nb_msg_non_lus, date_dernier_message);
	
					unGroupe.addTicketConnu(unTicket);
				}
			}
			retourne.add(unGroupe);
		}		
		return retourne;
	}



	@Override
	public int sqlInsertGroupe(String nom) throws SQLException {

		System.out.println(" ** Insert groupes ** ");
		
		Statement statement = conn.createStatement();
		String query = "INSERT INTO groupe (id_groupe, nom_groupe) VALUES (NULL, \""+ nom +"\")";
		statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);	
		ResultSet rs = statement.getGeneratedKeys();
		if (rs.next())
			return rs.getInt(1);
		return -1;
	}







	@Override
	public int sqlInsertUtilisateur(String nom, String prenom, String nickname,
			String pass) throws SQLException {
		
		System.out.println(" ** Insert utilisateur ** ");
		
	    Statement statement = conn.createStatement();
		String query = "INSERT INTO user (id_user, password_user, nickname_user, nom_user, prenom_user) "
				+ "VALUES (NULL, '"+Sha256.sha256("qt"+pass+"pi")+"','"+nickname+"', '"+nom+"', '"+prenom+"')";
		statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);	
		ResultSet rs = statement.getGeneratedKeys();
		if (rs.next())
			return rs.getInt(1);
		return -1;
	}







	@Override
	public int sqlInsertTicket(String titre, String premierMessage, int idUser, int idGroupe)
			throws SQLException {
		
		Statement statement = null;
		System.out.println(" ** insert ticket ** ");
	    java.sql.Date dateCurrent = new java.sql.Date(new Date().getTime());

	    statement = conn.createStatement();
		String query = "INSERT INTO ticket (id_ticket, titre_ticket, creation_ticket, id_groupe) VALUES (NULL, '"+titre+"', '"+dateCurrent+"', "+idGroupe+")";
		
		// On demande de renvoyer les clefs generees
		statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

		ResultSet rs = statement.getGeneratedKeys();
		int id_ticket = 0;
		if (rs.next()) {
			id_ticket = rs.getInt(1);
			sqlInsertMessage(premierMessage,idUser, id_ticket);	
		}

		return id_ticket;
	}







	@Override
	public int sqlInsertMessage(String contenu, int idUser, int idTicket)
			throws SQLException {	
		
		System.out.println(" ** Insert message ** ");
		
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
		
		initialiseStatutsDeLecture(idMsg);
		return idMsg;
	}










	@Override
	public int sqlUpdateGroupe(int id, String nom) throws SQLException {
	    Statement statement = conn.createStatement();
	    String query;
    	query = "UPDATE groupe SET nom_groupe = '"+ nom +"' WHERE id_groupe = "+ id +" LIMIT 1";
		statement.executeUpdate(query);	
		return id;
	}







	@Override
	public int sqlUpdateUtilisateur(int id, String nom, String prenom,String nickname, String pass) throws SQLException {
		Statement statement = conn.createStatement();
		String query = "UPDATE user SET nom_user = '"+nom+"' AND prenom_user = '"+prenom+"' AND nickname_user = '"+nickname+"' AND password_user = '"+Sha256.sha256("qt"+pass+"pi")+"' WHERE id_user = "+id+" LIMIT 1";
		System.out.println(query);
		statement.executeUpdate(query);
		return id;
	}
	


	@Override
	public int sqlUpdateTicket(int idTicket, String titre, int idGroupe) throws SQLException {
		Statement statement = conn.createStatement();
		String query = "UPDATE ticket SET titre_ticket = '"+titre+"' AND id_groupe = '"+idGroupe+"' WHERE id_ticket = "+idTicket+" LIMIT 1";
		System.out.println(query);
		statement.executeUpdate(query);
		return idTicket;
	}



	@Override
	public int updateMessage(int idMsg, String contenu, int idUser,	int idTicket) throws SQLException {
		Statement statement = conn.createStatement();
		String query = "UPDATE message SET contenu = '"+contenu+"' AND id_user = '"+idUser+"' AND id_ticket = '"+idTicket+"' WHERE id_message = "+idMsg+" LIMIT 1";
		System.out.println(query);
		statement.executeUpdate(query);
		return idMsg;
	}



	@Override
	public void sqlRejoindreGroupe(int idUser, int idGroupe) throws SQLException {
		Statement statement = conn.createStatement();
		java.sql.Date dateCurrent = new java.sql.Date(new Date().getTime());
		String query = "INSERT INTO appartenance (id_groupe, id_user, inscription) VALUES ("+idGroupe+","+idUser+",'"+dateCurrent+"')";
		System.out.println(query);
		statement.executeUpdate(query);
	}



	@Override
	public void sqlQuitterGroupe(int idUser, int idGroupe) throws SQLException {
		Statement statement = conn.createStatement();
		String query = "DELETE FROM appartenance WHERE id_groupe = "+idGroupe+" AND id_user = "+idUser+" LIMIT 1";
		System.out.println(query);
		statement.executeUpdate(query);
	}



	@Override
	public void deleteGroupe(int id) throws SQLException {
	    Statement statement2 = conn.createStatement();
		String query2 = "DELETE FROM appartenance WHERE id_groupe = "+ id;
		System.out.println(query2);
		statement2.executeUpdate(query2);

	    Statement statement3 = conn.createStatement();
		String query3 = "SELECT id_ticket FROM ticket WHERE id_groupe = "+ id;
		System.out.println(query3);
		ResultSet rs3 = statement3.executeQuery(query3);

		while(rs3.next()) {
		    Statement statement6 = conn.createStatement();
			String query6 = "SELECT id_message FROM message WHERE id_ticket = "+ rs3.getInt("id_ticket");
			System.out.println(query6);
			ResultSet rs6 = statement6.executeQuery(query6);

			while(rs6.next()) {
			    Statement statement7 = conn.createStatement();
				String query7 = "DELETE FROM statut WHERE id_message = "+ rs6.getInt("id_message");
				System.out.println(query7);
				statement7.executeUpdate(query7);	
			}

		    Statement statement4 = conn.createStatement();
			String query4 = "DELETE FROM message WHERE id_ticket = "+ rs3.getInt("id_ticket");
			System.out.println(query4);
			statement4.executeUpdate(query4);			
		}

	    Statement statement5 = conn.createStatement();
		String query5 = "DELETE FROM ticket WHERE id_groupe = "+ id;
		System.out.println(query5);
		statement5.executeUpdate(query5);			

	    Statement statement = conn.createStatement();
		String query = "DELETE FROM groupe WHERE id_groupe = "+ id +" LIMIT 1";
		System.out.println(query);
		statement.executeUpdate(query);
	}



	@Override
	public void deleteTicket(int id) throws SQLException {
	    Statement statement = conn.createStatement();
		String query = "DELETE FROM message WHERE id_ticket = "+ id;
		statement.executeUpdate(query);
		
	    Statement statement2 = conn.createStatement();
		String query2 = "DELETE FROM ticket WHERE id_ticket = "+ id +" LIMIT 1";
		statement2.executeUpdate(query2);
	}



	@Override
	public void deleteMessage(int id) throws SQLException {
	    Statement statement = conn.createStatement();
		String query = "DELETE FROM message WHERE id_message = "+ id +" LIMIT 1";
		statement.executeUpdate(query);
	}


	@Override
	public void deleteUtilisateur(int id) throws SQLException {
		Statement statement4 = conn.createStatement();
		String query4 = "DELETE FROM statut WHERE id_user = "+ id;
		statement4.executeUpdate(query4);

	    Statement statement2 = conn.createStatement();
		String query2 = "DELETE FROM appartenance WHERE id_user = "+ id;
		statement2.executeUpdate(query2);

	    Statement statement3 = conn.createStatement();
		String query3 = "DELETE FROM message WHERE id_user = "+ id;
		statement3.executeUpdate(query3);	

	    Statement statement = conn.createStatement();
		String query = "DELETE FROM user WHERE id_user = "+ id +" LIMIT 1";
		statement.executeUpdate(query);
	}	
}
