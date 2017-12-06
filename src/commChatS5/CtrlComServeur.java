/**
 * Fichier CtrlComServeur.java
 * @date 5 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

import java.io.Serializable;

import communication.ComAdresse;
import communication.ComException;
import communication.ComIdentification;
import communication.ComMessage;
import communication.ObservateurComServeur;
import communication.simple.SimpleControleurServeur;
import communication.simple.SimpleMessage;

/**
 * Contrôleur de communication authentifiée permettant de répondre 
 * aux demandes de liste des groups ou de ticket
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
						observateur.demandeTicket(client, (Integer) args[1])
						);
				break;
			default:
				reponse = new SimpleMessage.SimpleMessageInformation(TypeMessage.INCONNU);
			}
		} catch (ClassCastException | IndexOutOfBoundsException | NullPointerException e){
			reponse = new SimpleMessage.SimpleMessageInformation(TypeMessage.INCONNU);
		}
		return reponse;
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


}
