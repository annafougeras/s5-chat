package controleur;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import modele.Groupe;
import modele.Identifiable;
import modele.KeyIdentifiable;
import modele.Message;
import modele.StatutDeLecture;
import modele.Ticket;
import vue.BaseScreenClient;
import vue.ConnectionScreen;
import vue.MainScreen;
import app.ClientApp;

import commChatS5.CtrlComClient;
import commChatS5.ICtrlComClient;
import commChatS5.Identifiants;

/**
 *
 * @author Vincent Fougeras
 */
public class CtrlClient extends Observable implements ICtrlClient {


	/**
	 * Notification : utilisé pour notifier les classes observantes
	 */
	public enum Notification {
		UPDATE_JTREE,
		UPDATE_MESSAGES;
	}

	public class RunnableNotification implements Runnable {

		private Observable emetteur;
		private Object notification;

		public RunnableNotification(Observable emetteur, Object notification){
			this.emetteur = emetteur;
			this.notification = notification;
		}

		@Override
		public void run() {
			setChanged();
			notifyObservers(notification);
		}
	}



	ICtrlComClient ctrlComClient;
	BaseScreenClient currentScreen;
	NavigableSet<Groupe> model; /* Liste des groupes liés à l'utilisateur, qui contiennent des tickets, qui contiennent des messages ... */
	NavigableSet<Groupe> groupes; /* Liste de tous les groupes existants */

	Map<KeyIdentifiable,Groupe> groupesParId;
	Map<KeyIdentifiable,Ticket> ticketsParId;





	public CtrlClient(){
		// Crée le ctrlCom
		ctrlComClient = new CtrlComClient(this, ClientApp.ADRESSE_SERVEUR);

		// Crée le modèle
		this.model = new TreeSet<>(new Comparator<Groupe>() {
			@Override
			public int compare(Groupe g1, Groupe g2) {
				return g1.getNom().compareTo(g2.getNom());
			}
		});

		this.groupes = new TreeSet<>(new Comparator<Groupe>() {
			@Override
			public int compare(Groupe g1, Groupe g2) {
				return g1.getNom().compareTo(g2.getNom());
			}
		});


		// Crée la vue
		currentScreen = new ConnectionScreen(this);
		this.addObserver(currentScreen);
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				currentScreen.setVisible(true);
			}
		});
	}






	/* 
        Méthodes de ICtrlVue
	 */

	@Override
	public NavigableSet<Groupe> getModel(){

		// Le modèle est constitué des groupes ayant au moins un ticket visible
		if (groupes.size() == 0)
			getRemoteGroupes();

		model = new TreeSet<>();
		Iterator<Groupe> iter = groupes.iterator();
		for (;iter.hasNext();){
			Groupe currGroupe = iter.next();
			if (currGroupe.getTicketsConnus().size() > 0)
				model.add(currGroupe);
		}
		return model;
	}

	@Override
	public NavigableSet<Groupe> getGroupes(){
		if (groupes.size() == 0)
			getRemoteGroupes();
		return this.groupes;
	}

	

	@Override
	public void getRemoteMessages(Ticket ticket) {
		if (!ticket.estComplet())
			ctrlComClient.demanderTicket(ticket);
	}

	@Override
	public void getRemoteGroupes() {
		// Demande maintenant : bloquant
		majGroupes(ctrlComClient.demanderTousLesGroupesBloquant());
	}

	@Override
	public void addTicket(Groupe destination, String title, String content) {
		ctrlComClient.creerTicket(destination, title, content);
	}

	@Override
	public void addMessage(Ticket ticket, String message) {
		ctrlComClient.creerMessage(ticket, message);
	}

	@Override
	public boolean connecter(String idUser, String password) {

		System.out.println("Patientez...");

		Identifiants identifiants = new Identifiants(idUser, password);
		boolean connecte = false;

		connecte = ctrlComClient.etablirConnexionBloquant(identifiants);

		System.out.println("Connexion établie : " + connecte);

		if(connecte)
			this.changeScreen(new MainScreen(this));            

		return connecte;

	}


	@Override
	public void deconnecter() {
		ctrlComClient.deconnecter();
		groupes = new TreeSet<Groupe>();
		model = new TreeSet<Groupe>();
		this.changeScreen(new ConnectionScreen(this));
	}



	@Override
	public void informerLecture(Ticket ticket) {
		ctrlComClient.informerLecture(ticket, StatutDeLecture.LU);
	}

	// Méthodes inutilisées
	@Override
	@Deprecated
	public void getRemoteTickets() {
		throw new UnsupportedOperationException("Not supported yet.");
	}



	/*
	 * Méthodes privées
	 */


	/**
	 * Met à jour le modèle de l'application
	 * @param nouveauxGroupes 
	 */
	private void majGroupes(Set<Groupe> nouveauxGroupes){
		groupes = new TreeSet<>();
		groupesParId = new HashMap<>();
		ticketsParId = new HashMap<>();
		groupes.addAll(nouveauxGroupes);
		for (Groupe currGroupe: groupes){
			groupesParId.put(new KeyIdentifiable(currGroupe.getIdentifiantUnique()), currGroupe);
			for (Ticket currTicket: currGroupe.getTicketsConnus())
				ticketsParId.put(new KeyIdentifiable(currTicket.getIdentifiantUnique()), currTicket);
		}
	}
	

	/**
	 * Change l'écran visible
	 * @param newScreen le nouvel écran
	 */
	private void changeScreen(final BaseScreenClient newScreen){
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				currentScreen.setVisible(false);
				deleteObserver(currentScreen);

				// En séquence, sinon on risque de deleteObserver après changé currentScreen
				currentScreen = newScreen;
				addObserver(currentScreen);
				currentScreen.setVisible(true);
			}
		}); 
	}


	
	

	/* 
        Méthodes de S5Client
	 */
	
	
	@Override
	public void recevoir(Ticket ticketRecu) {
		
		int nbMessageRecusPourLaPremiereFois = 0;

		if (ticketRecu.getNbMessagesNonLus() > 0)
			ctrlComClient.informerLecture(ticketRecu, StatutDeLecture.RECU);

		//System.out.println("Ticket reçu : " + ticketRecu);

		Identifiable groupeParent = new KeyIdentifiable(ticketRecu.getParent());
		Identifiable ticket = new KeyIdentifiable(ticketRecu);

		// Le groupe parent est déjà connu
		if (groupesParId.containsKey(groupeParent)) {

			Groupe groupe = groupesParId.get(groupeParent);

			// Le ticket existe déjà : on le supprime
			if (ticketsParId.containsKey(ticket)) {
				groupe.getTicketsConnus().remove(ticketsParId.get(ticket));
				ticketsParId.remove(ticket);
			}

			// On ajoute le nouveau ticket
			groupe.getTicketsConnus().add(ticketRecu);
			ticketsParId.put(new KeyIdentifiable(ticketRecu), ticketRecu);

			java.awt.EventQueue.invokeLater(new RunnableNotification(this, ticketRecu));

		}
		else {
			// Le groupe est inconnu, on demande la liste des groupes
			//System.out.println("Groupe inconnu -> demandé");
			ctrlComClient.demanderTousLesGroupes();
		}

	}

	@Override
	public void recevoir(Set<Groupe> listeDesGroupes) {
		// Reçoit une nouvelle liste de groupes (à tout moment)
		//System.out.println("Liste des groupes reçue");
		majGroupes(listeDesGroupes);

		for (Groupe currGroupe: listeDesGroupes)
			for (Ticket currTicket: currGroupe.getTicketsConnus())
				ctrlComClient.informerLecture(currTicket, StatutDeLecture.RECU);

		java.awt.EventQueue.invokeLater(new RunnableNotification(this, Notification.UPDATE_JTREE));

	}

	@Override
	public void recevoir(Message messageRecu) {
		//System.out.println("Message reçu : " + messageRecu);
		Identifiable ticketParent = messageRecu.getParent();


		if (ticketsParId.containsKey(ticketParent)){

			Ticket ticket = ticketsParId.get(ticketParent);
			ticket.addMessage(messageRecu);

			ctrlComClient.informerLecture(ticket, StatutDeLecture.RECU);

			java.awt.EventQueue.invokeLater(new RunnableNotification(this, Notification.UPDATE_MESSAGES));
		}
		else {
			//System.out.println("Ticket inconnu -> demandé");
			ctrlComClient.demanderTicket(ticketParent);
		}

	}

	
	@Override
	public void recevoir(boolean accuseConnexion) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@Override
	public void recevoir(Object messageInconnu) {
		System.err.println("Message inconnu reçu : " + messageInconnu);
	}



}
