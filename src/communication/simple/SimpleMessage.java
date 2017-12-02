/**
 * Fichier SimpleMessage.java
 * @date 30 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication.simple;

import java.io.Serializable;

import communication.ComIdentification;
import communication.ComMessage;



/**
 * Message simple.
 * Transporte un tableau d'objets sérializables. Ne peut être instantié directement.
 * 
 * @see SimpleControleurClient
 * @see SimpleControleurServeur
 */
@SuppressWarnings("serial")
public class SimpleMessage implements ComMessage {
	
	/**
	 * Message échangé lors des ouvertures de connexion
	 * SimpleMessage
	 */
	public static class SimpleMessageBonjour extends SimpleMessage {
		public SimpleMessageBonjour(){
			super(SimpleTypeMessage.BONJOUR);
		}
	}
	
	/**
	 * Message transportant les informations de connexion
	 * SimpleMessage
	 */
	public static class SimpleMessageConnexion extends SimpleMessage {
		public SimpleMessageConnexion(ComIdentification identite){
			super(SimpleTypeMessage.IDENTIFICATION, identite);
		}
		public ComIdentification getIdentite(){
			return (ComIdentification) getArgs()[0];
		}
	}
	
	/**
	 * Message acquittant une ouverture de connexion
	 * SimpleMessage
	 */
	public static class SimpleMessageConfirmationConnexion extends SimpleMessage {
		public SimpleMessageConfirmationConnexion(Boolean succes){
			super(SimpleTypeMessage.CONFIRM_IDENTIFICATION, succes);
		}
		public Boolean getSuccesConnexion(){
			return (Boolean) getArgs()[0];
		}
	}
	
	/**
	 * Message transportant une information (n'attendant pas de réponse)
	 * SimpleMessage
	 */
	public static class SimpleMessageInformation extends SimpleMessage {
		public SimpleMessageInformation(Serializable... informations){
			super(SimpleTypeMessage.INFORME, informations);
		}
		public Object[] getInformation(){
			return getArgs();
		}
	}
	
	/**
	 * Message transportant une requête (attendant une réponse)
	 * SimpleMessage
	 */
	public static class SimpleMessageDemande extends SimpleMessage {
		public SimpleMessageDemande(Serializable... informations){
			super(SimpleTypeMessage.DEMANDE, informations);
		}
		public Object[] getDemande(){
			return getArgs();
		}
	}
	
	/**
	 * Message corrompu
	 * SimpleMessage
	 */
	public static class SimpleMessageInvalide extends SimpleMessage {
		public SimpleMessageInvalide(){
			super(SimpleTypeMessage.INVALIDE);
		}
	}
	
	
	
	
	
	
	
	private SimpleTypeMessage type;
	private Serializable args[];
	
	private SimpleMessage(SimpleTypeMessage type, Serializable... args){
		this.type = type;
		this.args = args;
	}
	
	/**
	 * Arguments transmis lors de la création du message
	 * @return Arguments
	 */
	public Serializable[] getArgs(){
		return this.args;
	}
	
	/**
	 * Type de message
	 * @return Type du message
	 */
	public SimpleTypeMessage getTypeMessage(){
		return type;
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("[" + getTypeMessage());
		for (Serializable arg: args)
			str.append("; " + arg);
		str.append("]");
		return str.toString();
	}

}
