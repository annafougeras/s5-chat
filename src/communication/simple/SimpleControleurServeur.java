/**
 * Fichier SimpleControleurServeur.java
 * @date 1 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication.simple;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import communication.ComAdresse;
import communication.ComException;
import communication.ComMessage;
import communication.ControleurComServeur;
import communication.ObservateurComServeur;
import communication.simple.SimpleMessage.SimpleMessageConfirmationConnexion;
import communication.simple.SimpleMessage.SimpleMessageConnexion;

/**
 * Contrôleur serveur manipulant des SimpleMessages
 * @see SimpleMessage
 */
public class SimpleControleurServeur extends AbstractSimpleControleur 
						implements ControleurComServeur<SimpleMessage> {
	
	
	public static final int NB_MAX_ECHECS_SUCCESSIFS = 3;
	
	
	private ObservateurComServeur<SimpleMessage> observateur;
	private ServerSocket socketAttenteClient;
	private Thread threadAttenteClients;
	private Set<Thread> threadsEcouteClients = new HashSet<>();
	private Map<ComAdresse,Socket> sockets = new HashMap<ComAdresse, Socket>();
	
	/**
	 * Constructeur
	 * @param observateur Objet à informer lors de la réception de messages
	 */
	public SimpleControleurServeur(ObservateurComServeur<SimpleMessage> observateur){
		this.observateur = observateur;
	}

	@Override
	public void start(int port) throws ComException {
		try {
			socketAttenteClient = new ServerSocket(port);
		} catch (IOException e) {
			throw new ComException("Echec création socket d'attente des clients", e);
		}
		threadAttenteClients = new Thread(new AttenteClient());
		threadAttenteClients.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void stop() throws ComException {
		try {
			// TODO Comprendre pourquoi ça ne marche pas avec autre chose que la méthode stop()
			threadAttenteClients.stop();
			socketAttenteClient.close();
			for (Thread t: threadsEcouteClients)
				t.stop();
			for(Entry<Socket, FluxObjets> entree: fluxObjets.entrySet()){
				System.out.println("Contrôleur: Fermeture connexion: " + 
						SimpleAdresse.distante(entree.getKey()));
				entree.getValue().ois.close();
				entree.getValue().oos.close();
				entree.getKey().close();
			}
		} catch (IOException e){
			throw new ComException(e);
		}
	}

	@Override
	public void informer(SimpleMessage message, ComAdresse destinataire) {
		// TODO Auto-generated method stub

	}

	@Override
	public void informerTous(SimpleMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void demander(SimpleMessage message, ComAdresse destinataire) {
		// TODO Auto-generated method stub

	}

	@Override
	public void demanderTous(SimpleMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<ComAdresse> tousLesClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deconnecter(ComAdresse client) {
		System.out.println("Contrôleur: Fermeture connexion: " + client);
		Socket socketClient = sockets.get(client);
		FluxObjets fluxClient = fluxObjets.get(socketClient);
		
		try {
			fluxClient.ois.close();
		} catch (IOException e) {
			System.err.println("Flux d'entrée client " + client + " déjà fermé");
		}
		try {
			fluxClient.oos.close();
		} catch (IOException e) {
			System.err.println("Flux de sortie client " + client + " déjà fermé");
		}
		try {
			socketClient.close();
		} catch (IOException e) {
			System.err.println("Socket client " + client + " déjà fermé");
		}
	}
	
	
	
	
	
	/**
	 * Attente de connexion entrantes
	 * SimpleControleurServeur
	 */
	private class AttenteClient implements Runnable {

		@Override
		public void run() {
			while (true){
				Socket socketClient;
				try {
					socketClient = socketAttenteClient.accept();
					Thread thread = new Thread(new ConnexionClient(socketClient));
					thread.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Connexion d'un client
	 * SimpleControleurServeur
	 */
	private class ConnexionClient implements Runnable {
		
		private Socket socket;
		
		public ConnexionClient(Socket socket){
			this.socket = socket;
		}

		@Override
		public void run() {
			// La connexion TCP est établie
			if (verbeux)
				System.out.println("Contrôleur: Connexion TCP établie avec " + 
						SimpleAdresse.distante(socket));
			
			try {
				
				ouvrirFlux(socket);

				
				// Reception des identifiants
				SimpleMessageConnexion msg = (SimpleMessageConnexion) recevoirMessage(socket);
				
				// Envoi d'un message de confirmation
				
				boolean cnxOK = observateur.ctrlCom_validerConnexion(
					SimpleAdresse.distante(socket), 
					msg.getIdentite()
					);
				ComMessage reponse = new SimpleMessageConfirmationConnexion(cnxOK);
				envoyerMessage(socket, reponse);
				
				// Ecoute du client ou fermeture du socket
				if (cnxOK){
					Thread thread = new Thread(new EcouteClient(socket));
					threadsEcouteClients.add(thread);
					thread.start();
				}
				else {
					socket.close();
				}
				
				System.out.println("Contrôleur: Connexion avec " 
						+ SimpleAdresse.distante(socket) + " : " + cnxOK);
				
				
			} catch (IOException e) {
				System.err.println("Contrôleur: Connexion : Echec création flux : " + e.getMessage());
			} catch (ClassCastException e) {
				System.err.println("Contrôleur: Réception message invalide " + e.getMessage());
			}
		}
	}
	
	/**
	 * Ecoute d'un client
	 * SimpleControleurServeur
	 */
	private class EcouteClient implements Runnable {

		private Socket socket;
		private ComAdresse adresseClient;
		
		/**
		 * @param socket
		 */
		public EcouteClient(Socket socket) {
			adresseClient = SimpleAdresse.distante(socket);
			sockets.put(adresseClient, socket);
			this.socket = socket;
		}

		@Override
		public void run() {
			
			int essaisRestants = NB_MAX_ECHECS_SUCCESSIFS;
			while (essaisRestants > 0){
				try {
					SimpleMessage message = recevoirMessage(socket);
					
					switch (message.getTypeMessage()){
					case INFORME:
						observateur.ctrlCom_informer(adresseClient, message);
						break;
					case DEMANDE:
						SimpleMessage reponse = (SimpleMessage) observateur.ctrlCom_recevoir(
								adresseClient, message);
						envoyerMessage(socket, reponse);
						break;
					default:
						throw new ComException.TypeMessageException(
								"Aucune action attachée au type "+message.getTypeMessage());
					}
					
				} catch (EOFException | SocketException e){
					System.err.println("Contrôleur: flux fermé brutalement ");
					essaisRestants = 0;
				} catch (ComException.TypeMessageException | ClassCastException e){
					System.err.println("Contrôleur: message illisible");
					e.printStackTrace();
					--essaisRestants;
				} catch (IOException e) {
					e.printStackTrace();
					--essaisRestants;
				}
			}
			deconnecter(adresseClient);
		}
		
	}

}
