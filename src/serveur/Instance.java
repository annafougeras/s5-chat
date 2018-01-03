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
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import modele.Groupe;
import modele.Identifiable;
import modele.KeyIdentifiable;
import modele.Message;
import modele.StatutDeLecture;
import modele.Ticket;
import modele.Utilisateur;

import commChatS5.Identifiants;
import commChatS5.S5Serveur;
import communication.ComAdresse;

public class Instance implements S5Serveur {
	
	static final String JDBC_DRIVER = "mysql.src.com.mysql.jdbc.Driver";  
	/*
	static final String DB_URL = "jdbc:mysql://databases.000webhost.com:3306/id4146242_chats5";
	static final String USER = "id4146242_chats5";
	static final String PASS = "root123";
	*/
	
	
	// DB locale
	static final String DB_URL = "jdbc:mysql://localhost:3306/projetS5";
	static final String USER = "s5";
	static final String PASS = "s5";
	
	
	
	// On garde l'association adresse réseau / identifiant
	Map<ComAdresse,String> utilisateurs = new HashMap<>();
	
	
	
	
	public Instance(){
		
		
		// Connexion à la db : une seule fois à l'instanciation ?
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
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
		
		System.out.println("mysql-connector-java-5.1.44-bin trouvé et chargé");
	}
	
	
	/* MAIN
	   public static void main(String[] args) {
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("mysql.src.com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Fin");
	   }
	 */
	private Connection conn;

	@Override // pour cette methode du coup faudrait créer une table spéciale (ou un champ reservé à ça, voir ça avec pierre)
	public boolean demandeConnexion(ComAdresse client, Identifiants identifiants) {
		// select * from users where nickname=identifiants.getNom() and pass=identifiants.getPass()
		// if ok :
		boolean ok = true;
		
		// On garde l'association ComAdresse/id
		if (ok)
			utilisateurs.put(client, identifiants.getNom());
		return ok;
	}

	@Override // je crois qu'il faut rajouter l'exception SQLException dans l'interface <- pas moi :-)
	// Pas d'exception SQL pour la communication réseau : il faut les traiter ici (retourne null par exemple)
	public Set<Groupe> demandeTousLesGroupes(ComAdresse client) {
		Set<Groupe> retourne = new TreeSet<>();
		try {
			//conn = null;
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
				
				String utilisateur = utilisateurs.get(client);
				
				// if est_dans_groupe(utilisateur) or nb_messages_dans_ticket(utilisateur) > 0
				
				// Recuperation des tickets pour chaque groupe
				// On renvoie des tickets incomplets (sans les messages)
				String sql2 = "SELECT * FROM ticket WHERE id_groupe = " + id_groupe;
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sql2);
				while(rs2.next()){
					int id_ticket = rs2.getInt("id_ticket");
					//Date dateCreation = rs2.getDate("creation_ticket");
					String titreTicket = rs2.getString("titre_ticket");
					//NavigableSet<Message> messages = new TreeSet<Message>();
					
					int nb_msg_non_lus = 0;
					// int nb_msg_non_lus = select count from bla bla bla
					Date date_dernier_message = new Date();
					// select date from blablabla

					// Avec le constructeur
					Ticket unTicket = new Ticket(id_ticket,titreTicket, nb_msg_non_lus, date_dernier_message);
	
					// Ca on le ne fait pas : les tickets sont incomplets. C'est le travail de demanderTicket
					/*
					// Recuperation des messages pour chaque ticket
					String sql3 = "SELECT id_message,contenu,user.id_user,nom_user,prenom_user,date_message FROM message, user WHERE message.id_user = user.id_user AND id_ticket = " + id_ticket + " ORDER BY date_message DESC";
					ResultSet rs3 = stmt.executeQuery(sql3);
					while(rs3.next()){
						int idMsg = rs3.getInt("id_message");
						String texteMsg = rs3.getString("contenu");
						Utilisateur emetteur = new Utilisateur(rs3.getString("id_user"), rs3.getString("nom_user"), rs3.getString("prenom_user"));
						Date dateEmission = rs3.getDate("date_message");
						
						// Avec le constructeur
						Message unMessage = new Message(idMsg, emetteur, texteMsg, dateEmission, null);
						unTicket.addMessage(unMessage);
					}
					rs3.close();
					*/
					unGroupe.addTicketConnu(unTicket);
				}
				stmt2.close();
				rs2.close();
				retourne.add(unGroupe);

			}		
			rs.close();
			stmt.close();
			//conn.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return retourne;
	}



	@Override
	public Ticket demandeTicket(ComAdresse client, Identifiable idTicket) {

		Ticket unTicket = null;
		// Id du client
		String utilisateur = utilisateurs.get(client);
		
		try {
			
			// si l'utilisateur a accès au ticket alors...
			
			
			//conn = null;
			Statement stmt = null;
			System.out.println(" ** Select ticket ** ");
			stmt = conn.createStatement();
			int id_ticket = idTicket.getIdentifiantNumeriqueUnique();
			String sql = "SELECT * FROM ticket WHERE id_ticket = " + id_ticket + " LIMIT 1";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				Date dateCreationTicket = rs.getDate("creation_ticket");
				String titreTicket = rs.getString("titre_ticket");
				int id_groupe_parent = rs.getInt("id_groupe");
				NavigableSet<Message> messages = new TreeSet<Message>();
	
				// Recuperation des messages pour chaque ticket
				String sql2 = "SELECT id_message,contenu,user.id_user,nom_user,prenom_user,date_message FROM message, user WHERE message.id_user = user.id_user AND id_ticket = " + id_ticket + " ORDER BY date_message DESC";
	
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sql2);
	
				while(rs2.next()){
					String contenuMsg = rs2.getString("contenu");
					Utilisateur emetteur = new Utilisateur(rs2.getString("id_user"), rs2.getString("nom_user"), rs2.getString("prenom_user"));
					Date dateMsg = rs2.getDate("date_message");
					
					// Ici on calcule les statuts de lecture pour les lecteurs du message
					NavigableMap<Utilisateur, StatutDeLecture> statuts = null;
	
					// Instanciation du message
					Message unMessage = new Message(0, emetteur, contenuMsg, dateMsg, statuts);
					unMessage.setParent(idTicket);
					messages.add(unMessage);
				}
				rs2.close();
				
				// On crée le ticket
				unTicket = new Ticket(id_ticket, titreTicket, messages, dateCreationTicket);
	
				// On renseigne son parent
				unTicket.setParent(new KeyIdentifiable(id_groupe_parent));
				
			}
			else {
				System.err.println("Identifiant de ticket inconnu (" + id_ticket + ")");
			}
			
			rs.close();
			stmt.close();
			//conn.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return unTicket;
	}


	
	@Override // je pense qu'on devrait pas retourner le ticket + il faudrait l'utilisateur en argument pour le premier message
	// L'utilisateur, c'est le client :-)
	public Ticket creationTicket(ComAdresse client, Identifiable groupe, String titre, String premierMessage) {
		//int id_user = 1; // !! a modifier !! \\
		String id_user = utilisateurs.get(client);
		
		Ticket unTicket = null;
		
		try {
			
			//conn = null;
			Statement statement = null;
			System.out.println(" ** insert ticket ** ");
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//Calendar calendar = Calendar.getInstance();
		    java.sql.Date dateCurrent = new java.sql.Date(new Date().getTime());
	
		    statement = conn.createStatement();
			String query = "INSERT INTO ticket (id_ticket, titre_ticket, creation_ticket, id_groupe) VALUES (NULL, '"+titre+"', '"+dateCurrent+"', "+groupe.getIdentifiantNumeriqueUnique()+")";
			
			// On demande de renvoyer les clés générées
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			
	
			/* au cas où l'autre methode d'insertion est moins fiable (même si elle est plus simple pour la recupération de l'AI
			String query = "INSERT INTO ticket (id_ticket, titre_ticket, creation_ticket, id_groupe) VALUES (NULL, ?, ?, ?)";
	
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString (1, titre);
			preparedStmt.setDate (2, DateCurrent);
			preparedStmt.setInt (3, groupe.getIdentifiantNumeriqueUnique());
			preparedStmt.execute();
			 */
	
			ResultSet rs = statement.getGeneratedKeys(); // cette ligne c'est pour avoir le numero du dernier auto increment du ticket créer
			int id_ticket = 0;
			if (rs.next())
				id_ticket = rs.getInt(1);
	
			// Ici problème de foreign key : la référence à l'utilisateur est son identifiant numérique (et non son identifiant alphanumérique).
			// Pourquoi deux identifiants ?
			String query2 = "INSERT INTO message (id_message, id_ticket, id_user, contenu, date_message) VALUES (NULL, ?, ?, ?, ?)";
	
			PreparedStatement preparedStmt = conn.prepareStatement(query2);
			preparedStmt.setInt (1, id_ticket);
			preparedStmt.setString (2, id_user);
			preparedStmt.setString (3, premierMessage);
			preparedStmt.setDate (4, dateCurrent);
			preparedStmt.execute();
	
			unTicket = demandeTicket(client, new KeyIdentifiable(id_ticket));
			
			//conn.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}

		return unTicket; // à potentiellement enlevé si on décide de ne pas renvoyer le ticket finalement
	}

	@Override
	public Message creationMessage(ComAdresse client, Identifiable ticket,
			String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur adminDemandeUtilisateur(ComAdresse admin,
			Identifiable idUtilisateur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Utilisateur> adminDemandeUtilisateurs(ComAdresse admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message adminDemandeMessage(ComAdresse admin, Identifiable idMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Message> adminDemandeMessages(ComAdresse admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ticket adminDemandeTicket(ComAdresse admin, Identifiable idTicket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Ticket> adminDemandeTickets(ComAdresse admin) {
		// TODO Auto-generated method stub
		return null;
	}



	// Ton code d'où je tire mes copier-collers
	/*
	@Override // je pense qu'on devrait pas retourner le ticket + il faudrait l'utilisateur en argument pour le premier message
	public Ticket creationTicket(ComAdresse client, Identifiable groupe, String titre, String premierMessage) {
		int id_user = 1; // !! a modifier !! \\
		
		conn = null;
		Statement statement = null;
		System.out.println(" ** insert ticket ** ");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
	    java.sql.Date DateCurrent = new java.sql.Date(calendar.getTime().getTime());

	    statement = conn.createStatement();
		String query = "INSERT INTO ticket (id_ticket, titre_ticket, creation_ticket, id_groupe) VALUES (NULL, "+titre+", "+DateCurrent+", "+groupe.getIdentifiantNumeriqueUnique()+")";
		statement.executeUpdate(query);
*/
		/* au cas où l'autre methode d'insertion est moins fiable (même si elle est plus simple pour la recupération de l'AI
		String query = "INSERT INTO ticket (id_ticket, titre_ticket, creation_ticket, id_groupe) VALUES (NULL, ?, ?, ?)";

		PreparedStatement preparedStmt = conn.prepareStatement(query);
		preparedStmt.setString (1, titre);
		preparedStmt.setDate (2, DateCurrent);
		preparedStmt.setInt (3, groupe.getIdentifiantNumeriqueUnique());
		preparedStmt.execute();
		 */
/*
		ResultSet rs = statement.getGeneratedKeys(); // cette ligne c'est pour avoir le numero du dernier auto increment du ticket créer
		int id_ticket = rs.getInt(1);

		String query2 = "INSERT INTO message (id_message, id_ticket, id_user, contenu, date_message) VALUES (NULL, ?, ?, ?, ?)";

		PreparedStatement preparedStmt = conn.prepareStatement(query2);
		preparedStmt.setInt (1, id_ticket);
		preparedStmt.setInt (2, id_user);
		preparedStmt.setString (3, premierMessage);
		preparedStmt.setDate (4, DateCurrent);
		preparedStmt.execute();

		conn.close();
		return unTicket; // à potentiellement enlevé si on décide de ne pas renvoyer le ticket finalement
	}

	@Override // pareil ici il faut l'id_user pour pouvoir créer le message
	public Message creationMessage(ComAdresse client, Identifiable ticket, String message) throws SQLException {
		int id_user = 1; // !! a modifier !! \\
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
	    java.sql.Date DateCurrent = new java.sql.Date(calendar.getTime().getTime());

		String query2 = "INSERT INTO message (id_message, id_ticket, id_user, contenu, date_message) VALUES (NULL, ?, ?, ?, ?)";
		
		PreparedStatement preparedStmt = conn.prepareStatement(query2);
		preparedStmt.setInt (1, ticket.getIdentifiantNumeriqueUnique());
		preparedStmt.setInt (2, id_user);
		preparedStmt.setString (3, message);
		preparedStmt.setDate (4, DateCurrent);
		preparedStmt.execute();

		preparedStmt.close();
		conn.close();
		return null;
	}

	@Override
	public Utilisateur adminDemandeUtilisateur(ComAdresse admin, Identifiable idUtilisateur) throws SQLException {
		conn = null;
		Statement stmt = null;
		stmt = conn.createStatement();
		String sql = "SELECT nom_user, prenom_user FROM user WHERE id_user = "+idUtilisateur.getIdentifiantNumeriqueUnique()+" LIMIT 1";
		ResultSet rs = stmt.executeQuery(sql);
		
		// ce serait peut être plus judicieux que l'identifiant unique soit l'id_user dans la bdd (donc un int et pas un string, à méditer)
		Utilisateur user = new Utilisateur(idUtilisateur.getIdentifiantUnique(),rs.getString("nom_user"),rs.getString("prenom_user"));

		rs.close();
		conn.close();
		return user;
	}

	@Override
	public Set<Utilisateur> adminDemandeUtilisateurs(ComAdresse admin) throws SQLException {
		Set<Utilisateur> listeUser = new TreeSet<>();
		conn = null;
		Statement stmt = null;
		stmt = conn.createStatement();
		String sql = "SELECT id_user, nom_user, prenom_user FROM user";
		ResultSet rs = stmt.executeQuery(sql);
		
		// j'ai mis l'idUnique en string pour satisfaire le prototype mais c'est un int dans la bdd, ça m'étonnerait que ça marche si facilement
		listeUser.add(new Utilisateur(rs.getString("id_user"),rs.getString("nom_user"),rs.getString("prenom_user")));
		
		stmt.close();
		conn.close();
		return listeUser;
	}

	@Override
	public Message adminDemandeMessage(ComAdresse admin, Identifiable idMessage) throws SQLException {
		conn = null;
		Statement stmt = null;

		String sql = "SELECT id_message,contenu,user.id_user,nom_user,prenom_user,date_message FROM message, user WHERE message.id_user = user.id_user AND id_message = " + idMessage.getIdentifiantNumeriqueUnique() + " LIMIT 1";
		ResultSet rs = stmt.executeQuery(sql);

		Message unMessage;
		unMessage.setTexte(rs.getString("contenu"));
		unMessage.setEmetteur(new Utilisateur(rs.getString("id_user"), rs.getString("nom_user"), rs.getString("prenom_user")));
		unMessage.setDateEmission(rs.getDate("date_message"));

		rs.close();
		conn.close();
		return unMessage;
	}

	@Override
	public Set<Message> adminDemandeMessages(ComAdresse admin) throws SQLException {
		conn = null;
		Statement stmt = null;

		Set<Message> listeMessages = new TreeSet<>();

		String sql = "SELECT contenu,user.id_user,nom_user,prenom_user,date_message FROM message,user WHERE user.id_user = message.id_user";
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			Message unMessage;
			unMessage.setTexte(rs.getString("contenu"));
			unMessage.setEmetteur(new Utilisateur(rs.getString("id_user"), rs.getString("nom_user"), rs.getString("prenom_user")));
			unMessage.setDateEmission(rs.getDate("date_message"));
			listeMessages.add(unMessage);
		}
		rs.close();
		conn.close();
		return listeMessages;
	}

	@Override
	public Ticket adminDemandeTicket(ComAdresse admin, Identifiable idTicket) throws SQLException {
		conn = null;
		Statement stmt = null;
		System.out.println(" ** Select ticket ** ");
		stmt = conn.createStatement();

		int id_ticket = idTicket.getIdentifiantNumeriqueUnique();
		Ticket unTicket;

		String sql = "SELECT * FROM ticket WHERE id_ticket = " + id_ticket + " LIMIT 1";
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()){
			unTicket.setDateCreation(rs.getDate("creation_ticket"));
			unTicket.setTitre(rs.getString("titre_ticket"));
			NavigableSet<Message> messages = new TreeSet<Message>();

			// Recuperation des messages pour le ticket
			String sql2 = "SELECT id_message,contenu,user.id_user,nom_user,prenom_user,date_message FROM message, user WHERE message.id_user = user.id_user AND id_ticket = " + id_ticket + " ORDER BY date_message DESC";
			ResultSet rs2 = stmt.executeQuery(sql2);
			while(rs2.next()){
				Message unMessage;
				unMessage.setTexte(rs2.getString("contenu"));
				unMessage.setEmetteur(new Utilisateur(rs2.getString("id_user"), rs2.getString("nom_user"), rs2.getString("prenom_user")));
				unMessage.setDateEmission(rs2.getDate("date_message"));
				messages.add(unMessage);
			}
			rs2.close();
			unTicket.addMessages(messages);
		}
		rs.close();
		stmt.close();
		conn.close();
		return unTicket;
	}

	@Override
	public Set<Ticket> adminDemandeTickets(ComAdresse admin) throws SQLException {
		conn = null;
		Statement stmt = null;

		Set<Message> listeTickets = new TreeSet<>();

		String sql = "SELECT id_ticket FROM ticket";
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			// ici j'ai un problème quand j'ai voulu me faciliter le travail, je ne peux pas instancier d'objet "Identifiable" :/
			Ticket unTicket = adminDemandeTicket(admin,rs.getInt("id_ticket"));
			listeTickets.add(unTicket);
		}
		rs.close();
		conn.close();
		return listeTickets;
	}
	*/

	@Override
	public Groupe adminDemandeGroupe(ComAdresse admin, Identifiable idGroupe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Groupe> adminDemandeGroupes(ComAdresse admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void adminSetUtilisateur(ComAdresse admin, Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminSetMessage(ComAdresse admin, Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminSetTicket(ComAdresse admin, Ticket ticket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminSetGroupe(ComAdresse admin, Groupe groupe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminSupprimerUtilisateur(ComAdresse admin, Identifiable idUtilisateur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminSupprimerMessage(ComAdresse admin, Identifiable idMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminSupprimerTicket(ComAdresse admin, Identifiable idTicket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminSupprimerGroupe(ComAdresse admin, Identifiable idGroupe) {
		// TODO Auto-generated method stub
		
	}



}