/**
 * Fichier CtrlComAdmin.java
 * @date 28 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.util.Set;

import modele.Groupe;
import modele.Identifiable;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

import communication.ComAdresse;
import communication.ComEtatDeConnexion;
import communication.ComException;
import communication.ObservateurComClient;
import communication.simple.SimpleControleurClient;
import communication.simple.SimpleMessage;
import communication.simple.SimpleTypeMessage;

/**
 * Contrôleur réseau pour l'appli admin
 */
public class CtrlComAdmin implements ICtrlComAdmin, ICtrlComAdmin2, ObservateurComClient<SimpleMessage> {

	S5Admin observateur;
	ComAdresse adresseServeur;
	SimpleControleurClient controleur;
	
	public CtrlComAdmin(S5Admin observateur, ComAdresse adresseServeur){
		this.observateur = observateur;
		this.controleur = new SimpleControleurClient(this);
		this.adresseServeur = adresseServeur;
	}
	
	
	

	// ICtrlComAdmin
	
	@Override
	public boolean etablirConnexionBloquant(Identifiants identifiants) {
		try {
			return controleur.connecterBloquant(
					adresseServeur, identifiants) == ComEtatDeConnexion.CONNECTE;
		} catch (ComException e) {
			return false;
		}
	}





	@Override
	public void etablirConnexion(Identifiants identifiants) {
		controleur.connecter(adresseServeur, identifiants);
	}

	
	


	@Override
	public void demanderUtilisateurs() {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.TOUS_UTILISATEURS
						));
	}

	@Override
	public void demanderMessages() {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.TOUS_MESSAGES
						));
	}

	@Override
	public void demanderTickets() {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.TOUS_TICKETS
						));
	}

	@Override
	public void demanderGroupes() {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.TOUS_GROUPES
						));
	}

	@Override
	public void insererUtilisateur(Utilisateur modUtilisateur) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.AJOUT_MODIF_UTILISATEUR,
						modUtilisateur
						));
	}

	@Override
	public void insererMessage(Message modMessage) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.AJOUT_MODIF_MESSAGE,
						modMessage
						));
	}

	@Override
	public void insererTicket(Ticket modTicket) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.AJOUT_MODIF_TICKET,
						modTicket
						));
	}

	@Override
	public void insererGroupe(Groupe modGroupe) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.AJOUT_MODIF_GROUPE,
						modGroupe
						));	
	}

	@Override
	public void supprimerUtilisateur(Identifiable element) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.SUPP_UTILISATEUR,
						element
						));	
	}

	@Override
	public void supprimerMessage(Identifiable element) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.SUPP_MESSAGE,
						element
						));	
	}

	@Override
	public void supprimerTicket(Identifiable element) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.SUPP_TICKET,
						element
						));	
	}

	@Override
	public void supprimerGroupe(Identifiable element) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.SUPP_GROUPE,
						element
						));	
	}

	@Override
	@Deprecated
	public String executerSQLBloquant(String commandeSQL) {
		SimpleMessage reponse = null;
		try {
			reponse = controleur.demanderBloquant(
					new SimpleMessage.SimpleMessageDemande(
							TypeMessage.REQUETE_ADMIN, 
							TypeMessageAdmin.SQL,
							commandeSQL
							));
		} catch (ComException e) {
			e.printStackTrace();
		}	
		
		String reponseSQL;
		
		try {
			reponseSQL = (String) reponse.getArgs()[0];
		}
		catch (NullPointerException | ClassCastException e) {
			System.err.println("Erreur récéption message: "+e.getMessage());
			reponseSQL = "";
		}
		
		return reponseSQL;
	}

	@Override
	@Deprecated
	public void executerSQL(String commandeSQL) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(
						TypeMessage.REQUETE_ADMIN, 
						TypeMessageAdmin.SQL,
						commandeSQL
						));	
	}

	@Override
	public void deconnecter() {
		controleur.deconnecter();
	}


	
	
	
	

	// ICtrlComAdmin2

	@Override
	public void executer(TAction action, TData data, Identifiable element) {
		try {
			switch (action){
			case INSERER:
			case MODIFIER:
				switch (data){
				case GROUPE:
					insererGroupe((Groupe) element);
					break;
				case MESSAGE:
					insererMessage((Message) element);
					break;
				case TICKET:
					insererTicket((Ticket) element);
					break;
				case UTILISATEUR:
					insererUtilisateur((Utilisateur) element);
					break;
				}
				break;
			case OBTENIR:
				switch (data){
				case GROUPE:
					demanderGroupes();
					break;
				case MESSAGE:
					demanderMessages();
					break;
				case TICKET:
					demanderTickets();
					break;
				case UTILISATEUR:
					demanderUtilisateurs();
					break;
				}
				break;
			case SUPPRIMER:
				switch (data){
				case GROUPE:
					supprimerGroupe(element);
					break;
				case MESSAGE:
					supprimerMessage(element);
					break;
				case TICKET:
					supprimerTicket(element);
					break;
				case UTILISATEUR:
					supprimerUtilisateur(element);
					break;
				}
				break;
			}
		}
		catch (ClassCastException e){
			System.err.println("Impossible d'exécuter cette action");
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
	
	// ObservableComClient
	
	
	
	@Override
	public void ctrlCom_connexionEtablie(boolean succes) {
		observateur.recevoir(succes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ctrlCom_recevoir(SimpleMessage message) {
		

		try {
			TypeMessage type;
			Object args[] = message.getArgs();		
		
			if (message.getTypeMessage() != SimpleTypeMessage.INFORME)
				throw new ComException.TypeMessageException("Le message reçu est de type " + message.getTypeMessage());
			type = (TypeMessage) args[0];
			
			if (type != TypeMessage.INFORM_ADMIN){
				throw new ComException.TypeMessageException("Le type du message reçu est " + type + " (et non INFORM_ADMIN");
			}
			
			TypeMessageAdmin typeAdm = (TypeMessageAdmin) args[0];
			switch (typeAdm){
			case UTILISATEUR:
				observateur.recevoirGroupe((Groupe) args[1]);
				break;
			case MESSAGE:
				observateur.recevoirMessage((Message) args[1]);
				break;
			case TICKET:
				observateur.recevoirTicket((Ticket) args[1]);
				break;
			case GROUPE:
				observateur.recevoirGroupe((Groupe) args[1]);
				break;
			case TOUS_UTILISATEURS:
				observateur.recevoirGroupe((Set<Groupe>) args[1]);
				break;
			case TOUS_MESSAGES:
				observateur.recevoirMessage((Set<Message>) args[1]);
				break;
			case TOUS_TICKETS:
				observateur.recevoirTicket((Set<Ticket>) args[1]);
				break;
			case TOUS_GROUPES:
				observateur.recevoirGroupe((Set<Groupe>) args[1]);
				break;
			default:
				throw new ComException.TypeMessageException("Le type du message ADMIN reçu est " + typeAdm + " (invalide)");
			}
			
			
			
		} catch (ComException.TypeMessageException e){
			System.err.println(e.getMessage());
			observateur.recevoirMessageInvalide(message);
		} catch (ClassCastException | IndexOutOfBoundsException | NullPointerException e) {
			e.printStackTrace();
			observateur.recevoirMessageInvalide(message);
		}
		
		
	}



}
