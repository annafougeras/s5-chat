/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import commChatS5.S5Client;
import java.util.NavigableSet;
import modele.Groupe;
import modele.Ticket;

/**
 *
 * @author Vincent Fougeras
 */
public interface ICtrlVue extends S5Client {
    
    /**
     * Retourne le modèle actuel de l'application
     * @return le modèle actuel de l'application
     */
    public NavigableSet<Groupe> getModel();
    
    /**
     * Retourne la liste de tous les groupes connus
     * @return la liste de tous les groupes connus
     */
    public NavigableSet<Groupe> getGroupes();
     
    /**
     * Récupère tous les tickets auquel a accès l'utilisateur
     * 
     */
    void getRemoteTickets();
    
    /**
     * Récupère la liste des messages du ticket spécifié
     * 
     * @param ticket
     */
    void getRemoteMessages(Ticket ticket);
    
    /**
     * Récupère la liste de tous les groupes existants
     */
    void getRemoteGroupes();    
    
    
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
    
    
    void informerLecture(Ticket ticket);
}
