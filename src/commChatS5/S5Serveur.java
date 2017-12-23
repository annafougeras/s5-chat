/**
 * Fichier S5Serveur.java
 * @date 23 déc. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package commChatS5;

/**
 * Interface pour utilisateur du contrôleur de communication serveur (listener)
 * Communication client-serveur et admin-serveur
 * 
 * Les interfaces sont divisées en :
 *  - S5ServeurClient : communication client-serveur
 *  - S5ServeurAdmin : communication admin-serveur
 */
public interface S5Serveur extends S5ServeurClient, S5ServeurAdmin {

}
