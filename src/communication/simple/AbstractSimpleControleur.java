/**
 * Fichier AbstractSimpleControleur.java
 * @date 2 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication.simple;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import communication.ComAdresse;
import communication.ComMessage;
import communication.simple.SimpleMessage.SimpleMessageBonjour;
import communication.simple.SimpleMessage.SimpleMessageInvalide;

/**
 * Implémentation de fonctions communes aux contrôleurs client et serveur
 */
public abstract class AbstractSimpleControleur {
	
	/**
	 * Structure de données contenant le flux entrant (ois) et sortant (oos)
	 */
	protected static class FluxObjets {
		protected ObjectInputStream ois;
		protected ObjectOutputStream oos;
		
		public String toString(){
			return "[" + ois + ";" + oos + "]";
		}
	}
	
	protected Map<Socket,FluxObjets> fluxObjets;
	protected boolean verbeux;

	/**
	 * Constructeur
	 */
	public AbstractSimpleControleur(){
		fluxObjets = new HashMap<>();
		this.verbeux = false;
	}
	
	/**
	 * Constructeur
	 * @param verbeux Activer le mode verbeux
	 */
	public AbstractSimpleControleur(boolean verbeux){
		fluxObjets = new HashMap<>();
		this.verbeux = verbeux;
	}
	
	/**
	 * Ouvrir les flux entrants et sortants sur un socket
	 * @param socket Le socket à utiliser
	 * @return Flux entrants et sortants
	 * @throws IOException
	 */
	protected FluxObjets ouvrirFlux(Socket socket) throws IOException{
		FluxObjets flux = new FluxObjets();
		flux.oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		envoyerMessage(
				flux.oos, 
				new SimpleMessageBonjour(), 
				SimpleAdresse.distante(socket)
				);

		flux.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		SimpleMessage bonjour = null;
		bonjour = recevoirMessage(flux.ois, SimpleAdresse.distante(socket));
		if (bonjour == null || bonjour.getTypeMessage() != SimpleTypeMessage.BONJOUR)
			throw new IOException("N'a pas reçu de bonjour valide !");
		
		fluxObjets.put(socket, flux);
		if (verbeux)
			System.out.println("Contrôleur: Flux ouverts depuis et vers " + SimpleAdresse.distante(socket));
		return flux;
	}
	
	private void envoyerMessage(ObjectOutputStream oos, ComMessage msg, ComAdresse dest) throws IOException {
		oos.writeObject(msg);
		oos.flush();
		if (verbeux) 
			System.out.println("Contrôleur: Envoi vers " + dest + " : " + msg);
	}
	
	/**
	 * Envoyer un message sur un socket
	 * @param socket Socket à utiliser
	 * @param msg Message à envoyer
	 * @throws IOException
	 */
	protected void envoyerMessage(Socket socket, ComMessage msg) throws IOException {
		envoyerMessage(fluxObjets.get(socket).oos, msg, SimpleAdresse.distante(socket));
	}
	
	
	
	
	private SimpleMessage recevoirMessage(ObjectInputStream ois, ComAdresse src) throws IOException {
		SimpleMessage msg;
		try {
			msg = (SimpleMessage) ois.readObject();
		} catch (ClassNotFoundException e) {
			msg = new SimpleMessageInvalide();
		}
		if (verbeux)
			System.out.println("Contrôleur: Réception de " + src + " : " + msg);
		return msg;
		
	}
	/**
	 * Recevoir un message sur un socket (bloquant)
	 * @param socket Socket à utiliser
	 * @return Réponse reçue
	 * @throws IOException
	 */
	protected SimpleMessage recevoirMessage(Socket socket) throws IOException{
		return recevoirMessage(fluxObjets.get(socket).ois, SimpleAdresse.distante(socket));
	}
	
	
}
