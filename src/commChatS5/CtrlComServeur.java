/**
 * Fichier CtrlComServeur.java
 * @date 5 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.io.Serializable;
import java.util.Set;

import modele.Identifiable;
import modele.Message;
import modele.Ticket;

import communication.ComAdresse;
import communication.ComException;
import communication.ComIdentification;
import communication.ComMessage;
import communication.ObservateurComServeur;
import communication.simple.SimpleControleurServeur;
import communication.simple.SimpleMessage;

/**
 * Contrôleur de communication authentifiée permettant de répondre 
 * aux demandes de liste des groupes ou de ticket (appli client) et aux
 * requêtes de l'appli admin
 */
public class CtrlComServeur implements ICtrlComServeur, ObservateurComServeur<SimpleMessage> {

	
	private int port;
	private SimpleControleurServeur controleur;
	private S5ServeurClient observateur;
	
	
	/**
	 * Constructeur
	 * @param observateur Listener 
	 * @param port Port local d'écoute du serveur
	 */
	public CtrlComServeur(S5ServeurClient observateur, int port){
		this.observateur = observateur;
		this.port = port;
		controleur = new SimpleControleurServeur(this);
	}
	
	
	
	
	
	
	@Override
	public void start() {
		try {
			controleur.start(port);
		} catch (ComException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			controleur.stop();
		} catch (ComException e) {
			e.printStackTrace();
		}
	}


	@Override
	public Set<ComAdresse> getClientsConnectes() {
		return controleur.tousLesClients();
	}


	@Override
	public void informer(ComAdresse client, Ticket nouveauTicket) {
		SimpleMessage message = new SimpleMessage.SimpleMessageInformation(
				TypeMessage.INFORME_TICKET,
				nouveauTicket);
		controleur.informer(message, client);
	}


	@Override
	public void informer(ComAdresse client, Message nouveauMessage, Identifiable referenceTicket) {
		SimpleMessage message = new SimpleMessage.SimpleMessageInformation(
				TypeMessage.INFORME_MESSAGE,
				nouveauMessage,
				referenceTicket);
		controleur.informer(message, client);
	}

	
	
	
	
	
	
	
	// Méthodes du contrôleur générique

	@Override
	public boolean ctrlCom_validerConnexion(ComAdresse client,
			ComIdentification identifiants) {
		boolean b;
		try {
			b = observateur.demandeConnexion(client, (Identifiants) identifiants);
		} catch (ClassCastException e){
			b = false;
		}
		return b;
	}

	@Override
	public void ctrlCom_informer(ComAdresse client, SimpleMessage message) {
		System.out.println(client + ": " + message);
		System.out.println("Non implémenté");
	}

	@Override
	public ComMessage ctrlCom_recevoir(ComAdresse client, SimpleMessage requete) {
		SimpleMessage reponse;
		try {
			Object args[] = requete.getArgs();
			TypeMessage type = (TypeMessage) args[0];
			switch (type){
			
			case REQUETE_LISTE_GROUPE:
				reponse = new SimpleMessage.SimpleMessageInformation(
						TypeMessage.INFORME_LISTE_GROUPE,
						(Serializable) observateur.demandeTousLesGroupes(client)
						);
				break;
			
			case REQUETE_TICKET:
				reponse = new SimpleMessage.SimpleMessageInformation(
						TypeMessage.INFORME_TICKET,
						observateur.demandeTicket(client, (Identifiable) args[1])
						);
				break;
			
			case REQUETE_NOUVEAU_MESSAGE:
				Identifiable idTicket = (Identifiable) args[1];
				String contenuMessage = (String) args[2];
				reponse = new SimpleMessage.SimpleMessageInformation(
						TypeMessage.INFORME_MESSAGE,
						observateur.creationMessage(client, idTicket, contenuMessage)
						);
				break;
			
			case REQUETE_NOUVEAU_TICKET:
				Identifiable idGroupe = (Identifiable) args[1];
				String titreTicket = (String) args[2];
				String contenuPremierMessage = (String) args[3];
				reponse = new SimpleMessage.SimpleMessageInformation(
						TypeMessage.INFORME_TICKET,
						observateur.creationTicket(client, idGroupe, titreTicket, contenuPremierMessage)
						);
				break;
			
			default:
				reponse = new SimpleMessage.SimpleMessageInformation(TypeMessage.INCONNU);
				break;
				
			}
		} catch (ClassCastException | IndexOutOfBoundsException | NullPointerException e){
			System.err.println(e.getMessage());
			reponse = new SimpleMessage.SimpleMessageInformation(TypeMessage.INCONNU);
		}
		return reponse;
	}
	
	
	
	
	
	

}
