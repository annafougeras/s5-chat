/**
 * Fichier S5Serveur.java
 * @date 23 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

/**
 * Interface a implémenter par la partie logique du serveur.
 * Primitives permettant de répondre aux demandes de connexions, de consultation
 * ou de modification des informations de la base de données
 * 
 * Les interfaces sont divisées en :
 *  - S5ServeurClient : communication client-serveur
 *  - S5ServeurAdmin : communication admin-serveur
 */
public interface S5Serveur extends S5ServeurClient, S5ServeurAdmin {

}
