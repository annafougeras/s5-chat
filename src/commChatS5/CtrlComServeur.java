/**
 * Fichier CtrlComServeur.java
 * @date 5 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.io.Serializable;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import modele.Groupe;
import modele.Identifiable;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

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
	private S5Serveur observateur;
	
	
	/**
	 * Constructeur
	 * @param observateur Listener 
	 * @param port Port local d'écoute du serveur
	 */
	public CtrlComServeur(S5Serveur observateur, int port){
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
			e.printStackTrace();
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
		TypeMessageAdmin typeAdmin;
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
			
			case REQUETE_ADMIN:
				typeAdmin = (TypeMessageAdmin) args[1];
				switch (typeAdmin) {
				case GROUPE:
					Groupe g = observateur.adminDemandeGroupe(client, (Identifiable) args[2]);
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.GROUPE,
							g
							);
					break;
				case TICKET:
					Ticket t = observateur.adminDemandeTicket(client, (Identifiable) args[2]);
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.GROUPE,
							t
							);
					break;
				case UTILISATEUR:
					Utilisateur u = observateur.adminDemandeUtilisateur(client, (Identifiable) args[2]);
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.GROUPE,
							u
							);
					break;
				case TOUS_GROUPES:
					Set<Groupe> set = observateur.adminDemandeGroupes(client);
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.TOUS_GROUPES,
							new TreeSet<Groupe>(set)
							);
					break;
					
				case TOUS_TICKETS:
					Set<Ticket> set1 = observateur.adminDemandeTickets(client);
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.TOUS_GROUPES,
							new TreeSet<Ticket>(set1)
							);
					break;
					
				case TOUS_UTILISATEURS:
					Set<Utilisateur> set2 = observateur.adminDemandeUtilisateurs(client);
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.TOUS_UTILISATEURS,
							new TreeSet<Utilisateur>(set2)
							);
					break;

				case TOUS_UTILISATEURS_PAR_GROUPE:
					TreeMap<Groupe,NavigableSet<Utilisateur>> map = new TreeMap<>();
					map.putAll(observateur.adminDemandeUtilisateursParGroupe(client));
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.TOUS_UTILISATEURS_PAR_GROUPE,
							map
							);
					break;
					
				case AJOUT_MODIF_GROUPE:
					Groupe g1 = observateur.adminSetGroupe(client, (Groupe) args[2]);
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.GROUPE,
							g1
							);
					break;
					
				case AJOUT_MODIF_TICKET:
					Ticket t1 = observateur.adminSetTicket(client, (Ticket) args[2]);
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.TICKET,
							t1
							);
					break;
					
				case AJOUT_MODIF_UTILISATEUR:
					@SuppressWarnings("unused")
					Utilisateur u1 = observateur.adminSetUtilisateur(client, (Utilisateur) args[2]);
					TreeMap<Groupe,NavigableSet<Utilisateur>> map1 = new TreeMap<>();
					map1.putAll(observateur.adminDemandeUtilisateursParGroupe(client));
					reponse = new SimpleMessage.SimpleMessageInformation(
							TypeMessage.INFORM_ADMIN,
							TypeMessageAdmin.TOUS_UTILISATEURS_PAR_GROUPE,
							map1
							);
					break;
						
				default:
					System.out.println("Ce message n'est pas traité : " + type + "-" + typeAdmin);
					reponse = new SimpleMessage.SimpleMessageInformation(TypeMessage.INCONNU);
					break;
				}
				break;
			
			case INFORM_ADMIN:

				typeAdmin = (TypeMessageAdmin) args[1];
				reponse = null;
				switch (typeAdmin) {
					
				case SUPP_GROUPE:
					observateur.adminSupprimerGroupe(
							client, 
							(Identifiable) args[2]
									);
					break;
					
				case SUPP_TICKET:
					observateur.adminSupprimerTicket(
							client, 
							(Identifiable) args[2]
									);
					break;
					
				case SUPP_MESSAGE:
					observateur.adminSupprimerMessage(
							client, 
							(Identifiable) args[2]
									);
					break;
					
				case SUPP_UTILISATEUR:
					observateur.adminSupprimerUtilisateur(
							client, 
							(Identifiable) args[2]
									);
					break;
					
				case REJOINDRE_GROUPE:
					observateur.adminRejoindreGroupe(
							client, 
							(Identifiable) args[2], 
							(Identifiable) args[3]
									);

					
				case QUITTER_GROUPE:
					observateur.adminQuitterGroupe(
							client, 
							(Identifiable) args[2], 
							(Identifiable) args[3]
									);
					
				default:
					System.out.println("Ce message n'est pas traité : " + type + "-" + typeAdmin);
					reponse = new SimpleMessage.SimpleMessageInformation(TypeMessage.INCONNU);
					break;
				}
				break;
			
			default:
				System.out.println("Ce message n'est pas traité : " + type);
				reponse = new SimpleMessage.SimpleMessageInformation(TypeMessage.INCONNU);
				break;
				
			}
		} catch (ClassCastException | IndexOutOfBoundsException | NullPointerException e){
			System.err.println("Erreur dans la fabrication de la réponse : ");
			e.printStackTrace();
			System.err.println("Réponse avec le message INCONNU");
			reponse = new SimpleMessage.SimpleMessageInformation(TypeMessage.INCONNU);
		}
		return reponse;
	}
	
	
	
	
	
	

}
