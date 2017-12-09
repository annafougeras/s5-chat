/**
 * Fichier CtrlComClient.java
 * @date 4 déc. 2017
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

import communication.ComAdresse;
import communication.ComEtatDeConnexion;
import communication.ComException;
import communication.ObservateurComClient;
import communication.simple.SimpleControleurClient;
import communication.simple.SimpleMessage;
import communication.simple.SimpleMessage.SimpleMessageDemande;
import communication.simple.SimpleTypeMessage;

/**
 * 
 */
public class CtrlComClient implements ICtrlComClient, ObservateurComClient<SimpleMessage>{
	
	S5Client observateur;
	ComAdresse adresseServeur;
	SimpleControleurClient controleur;
	
	public CtrlComClient(S5Client observateur, ComAdresse adresseServeur){
		this.observateur = observateur;
		this.controleur = new SimpleControleurClient(this);
		this.adresseServeur = adresseServeur;
	}
	
	
	

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
	public Ticket demanderTicketBloquant(Identifiable idTicket) {
		SimpleMessage reponse;
		try {
			reponse = controleur.demanderBloquant(
					new SimpleMessage.SimpleMessageDemande(TypeMessage.REQUETE_TICKET, idTicket));
		} catch (ComException e) {
			e.printStackTrace();
			return null;
		}
		Object args[] = reponse.getArgs();
		try {
			if (args[0] != TypeMessage.INFORME_TICKET)
				return null;
			return (Ticket) args[1];
		}
		catch (ClassCastException | IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}





	@Override
	public void demanderTicket(Identifiable idTicket) {
		controleur.demander(
				new SimpleMessage.SimpleMessageDemande(TypeMessage.REQUETE_TICKET, idTicket));
	}





	@SuppressWarnings("unchecked")
	@Override
	public Set<Groupe> demanderTousLesGroupesBloquant() {
		SimpleMessage reponse = null;
		try {
			reponse = controleur.demanderBloquant(
					new SimpleMessage.SimpleMessageDemande(TypeMessage.REQUETE_LISTE_GROUPE));
		} catch (ComException e) {
			e.printStackTrace();
			return null;
		}
		try {
			Object args[] = reponse.getArgs();
			if (args[0] != TypeMessage.INFORME_LISTE_GROUPE)
				return null;
			return (Set<Groupe>) args[1];
		} catch (ClassCastException | IndexOutOfBoundsException | NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}





	@Override
	public void demanderTousLesGroupes() {
		controleur.demander(new SimpleMessageDemande(TypeMessage.REQUETE_LISTE_GROUPE));
	}


	@Override
	public void deconnecter() {
		controleur.deconnecter();
	}
	@Override
	public void creerTicket(Identifiable groupe, String titre,
			String premierMessage) {
		controleur.demander(new SimpleMessageDemande(
				TypeMessage.REQUETE_NOUVEAU_TICKET,
				groupe,
				titre,
				premierMessage
				));
	}




	@Override
	public void creerMessage(Identifiable ticket, String message) {
		controleur.demander(new SimpleMessageDemande(
				TypeMessage.REQUETE_NOUVEAU_MESSAGE,
				ticket,
				message
				));
	}


	
	
	
	
	
	
	
	
	// Méthodes du contrôleur générique


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
			switch (type){
			
			case INFORME_LISTE_GROUPE:
				observateur.recevoir((Set<Groupe>) args[1]);
				break;
			
			case INFORME_TICKET:
				observateur.recevoir((Ticket) args[1]);
				break;
			
			case INFORME_MESSAGE:
				observateur.recevoir((Message) args[1]);
				break;
			
			default:
				observateur.recevoir((Object) args);
				break;
				
			}
		} catch (ComException.TypeMessageException e){
			System.err.println(e.getMessage());
			observateur.recevoir((Object) null);
		} catch (ClassCastException | IndexOutOfBoundsException | NullPointerException e) {
			e.printStackTrace();
			observateur.recevoir((Object) null);
		}
		
	}






}
