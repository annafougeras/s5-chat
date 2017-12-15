/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import commChatS5.S5Client;
import modele.Groupe;
import modele.Ticket;

/**
 *
 * @author Vincent Fougeras
 */
public interface ICtrlVue extends S5Client {
    
    /**
     * Récupère tous les tickets auquel a accès l'utilisateur
     * 
     */
    void getTickets();
    
    /**
     * Récupère la liste des messages du ticket spécifié
     * 
     * @param ticket
     */
    void getMessages(Ticket ticket);
    
    /**
     * Récupère la liste de tous les groupes existants
     */
    void getGroupes();    
    
    /**
     * Ajoute un ticket
     * 
     * @param destination le groupe auquel est destiné ce ticket
     * @param content le contenu du premier message de ce ticket
     * @param title le titre du ticket
     */
    void addTicket(Groupe destination, String content, String title);
    
    /**
     * Ajoute un message sur un ticket
     * 
     * @param ticket le ticket dans lequel on souhaite ajouter un message
     * @param message le contenu du message
     */
    void addMessage(Ticket ticket, String message);
    
    /**
     * Tente la connection au serveur. Si la connection a lieu, on change la vue pour passer au MainScreen.
     * Si la connection n'a pas lieu, on renvoie false.
     * 
     * @param idUser
     * @param password
     * @return false si les identifiants sont incorrects
     */
    boolean connecter(String idUser, String password);
    
    /**
     * Se déconnecte du serveur. Change la vue pour passer au ConnectionScreen.
     * 
     */
    void deconnecter();
}
