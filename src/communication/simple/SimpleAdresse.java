/**
 * Fichier SimpleAdresse.java
 * @date 28 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package communication.simple;

import java.net.Socket;

import communication.ComAdresse;


/**
 * Adresse réseau d'un contrôleur client ou serveur
 */
public class SimpleAdresse implements ComAdresse, Comparable<ComAdresse>{
	
	
	public static SimpleAdresse distante(Socket socket){
		return new SimpleAdresse(
				socket.getInetAddress().getHostAddress(),
				socket.getPort());
	}
	
	
	private String adresse;
	private int port;
	
	/**
	 * Constructeur
	 * @param adresse Adresse
	 * @param port Port
	 */
	public SimpleAdresse(String adresse, int port){
		this.adresse = adresse;
		this.port = port;
	}

	/**
	 * Obtenir l'adresse
	 * @return L'adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * Obtenir le port
	 * @return Le port
	 */
	public int getPort() {
		return port;
	}
	
	@Override
	public String toString(){
		return adresse+":"+port;
	}

	@Override
	public int compareTo(ComAdresse autre) {
		int cmp = getAdresse().compareTo(autre.getAdresse());
		if (cmp == 0)
			cmp = getPort() - autre.getPort();
		return cmp;
	}
	
	@Override
	public boolean equals(Object obj){
		if (getClass().equals(obj.getClass()))
			return compareTo((ComAdresse) obj) == 0;
		return false;
	}
	
	@Override
	public int hashCode(){
		return 37 * getAdresse().hashCode() * getPort();
	}
	
	

}
