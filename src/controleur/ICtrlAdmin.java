package controleur;

import java.util.NavigableSet;

import modele.Groupe;
import modele.Utilisateur;

import commChatS5.S5Admin;

/**
 *
 * @author Vincent Fougeras
 */
public interface ICtrlAdmin extends S5Admin {

	/**
	 * Tente un connexion au serveur
	 * @param passAdmin le mot de passe pour se connecter au serveur
	 * @return true si la connexion a réussi
	 */
	public boolean connecter(String passAdmin);

	/**
	 * Se déconnecte du serveur
	 */
	public void deconnecter();

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
	public void insererUtilisateur(String nom, String prenom, String idUnique);


	/**
	 * Insère un groupe
	 * @param groupe Le groupe modifié / nouveau (remplace si id existant, crée sinon)
	 */
	public void insererGroupe(Groupe groupe);
	public void insererGroupe(String nom);




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
