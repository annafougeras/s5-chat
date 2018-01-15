/**
 * Fichier StatutDeLecture.java
 * @date 24 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         NÂ° Ã©tudiant 20 40 32 63
 */
package modele;

/**
 * DiffÃ©rentes valeurs du statut que peut avoir un message ou
 * un utilisateur vis-Ã -vis d'un message
 */
public enum StatutDeLecture {

	NON_ENVOYE,
	ENVOYE,
	RECU,
	LU;
	
	public String toString() {
		return "[" + name().toLowerCase() + "]";
	}
	
	
	public int toInt() {
		switch (this) {
		case ENVOYE: return 0;
		case LU: return 1;
		case RECU: return 2;
		default: return 4;
		}
	}
	
	public static StatutDeLecture fromInt(int i){
		switch (i) {
		case 0: return StatutDeLecture.ENVOYE;
		case 1: return StatutDeLecture.LU;
		case 2: return StatutDeLecture.RECU;
		default: return StatutDeLecture.NON_ENVOYE;
		}
	}
}
