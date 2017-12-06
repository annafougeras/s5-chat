/**
 * Fichier TestComExtracteurSimpleMessage.java
 * @date 2 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package tests;

import communication.simple.SimpleMessage;

/**
 * Protocole : le message contient un argument qui est de type String
 */
public class TestComExtracteurSimpleMessage {
	
	public static String getString(SimpleMessage msg){
		return (String) msg.getArgs()[0];
	}

}
