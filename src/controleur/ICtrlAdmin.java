/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import commChatS5.S5Admin;
import java.util.NavigableSet;
import modele.Groupe;
import modele.Identifiable;
import modele.Message;
import modele.Ticket;
import modele.Utilisateur;

/**
 *
 * @author Vincent Fougeras
 */
public interface ICtrlAdmin extends S5Admin {
    
    /**
     * Retourne la liste des groupes
     * @return la liste des groupes
     */
    public NavigableSet<Groupe> getGroupes();
    
    /**
     * Retourne la liste des groupes auquel appartient l'utilisateur
     * @return la liste des groupes auquel appartient l'utilisateur
     */
    public NavigableSet<Groupe> getGroupes(Utilisateur utilisateur);
    
    /**
     * Retourne la liste des utilisateurs
     * @return la liste des utilisateurs
     */
    public NavigableSet<Utilisateur> getUtilisateurs();
    
    /**
     * Retourne la liste des utilisateurs appartenant au groupe
     * @return la liste des utilisateurs appartenant au groupe
     */
    public NavigableSet<Utilisateur> getUtilisateurs(Groupe groupe);
        
    
    
    
    /**
     * Récupère la liste de tous les groupes existants
     */
    public void getRemoteGroupes();    
    
    
    /**
     * Récupère la liste de tous les utilisateurs existants
     */
    public void getRemoteUtilisateurs();    
    
    
    /**
     * Retire l'utilisateur du groupe
     * @param utilisateur
     * @param groupe 
     */
    public void removeUtilisateurFromGroupe(Utilisateur utilisateur, Groupe groupe);
    
    /**
     * Ajoute l'utilisateur au groupe
     * @param utilisateur
     * @param groupe 
     */
    public void addUtilisateurToGroupe(Utilisateur utilisateur, Groupe groupe);
    
    
    
    
    /**
    * Insère un utilisateur
    * @param utilisateur L'utilisateur modifié / nouveau (remplace si id existant, crée sinon)
    */
    public void insererUtilisateur(Utilisateur utilisateur);

    /**
    * Insère un groupe
    * @param groupe Le groupe modifié / nouveau (remplace si id existant, crée sinon)
    */
    public void insererGroupe(Groupe groupe);


    
    
    /**
    * Supprime un utilisateur
    * @param utilisateur
    */
    public void supprimerUtilisateur(Utilisateur utilisateur);

    /**
    * Supprime un groupe
    * @param groupe
    */
    public void supprimerGroupe(Groupe groupe);

}
