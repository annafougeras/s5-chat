/**
 * Fichier TestModele.java
 * @date 23 nov. 2017
 * @author Pierre POMERET-COQUOT
 *         pierre.pomeret@univ-tlse3.fr
 *         N° étudiant 20 40 32 63
 */
package tests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NavigableSet;
import java.util.TreeSet;

import modele.Groupe;
import modele.Message;
import modele.LacunaireException;
import modele.StatutDeLecture;
import modele.Ticket;
import modele.Utilisateur;

/**
 * Test des composants du package modele
 */
public class TestModele {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
		
		NavigableSet<Groupe> groupes = new TreeSet<>();
		NavigableSet<Message> messages = new TreeSet<>();
		Groupe g = new Groupe(7, "Mon groupe");
		
		
		groupes.add(new Groupe(5, "abc groupe"));
		groupes.add(new Groupe(1, "sdf"));
		groupes.add(new Groupe(10, "dfg"));
		
		groupes.add(g);
		
		try {
			g.addTicketsConnus(
					new Ticket(1, "ticket1", 4, df.parse("01/01/2017 12:05:42")),
					new Ticket(154, "ticket2", 0, new Date())
					);
			
			messages.add(new Message(4, new Utilisateur(1, "Michel"), "Coucou", df.parse("26/05/2017 15:20:12"), StatutDeLecture.LU, StatutDeLecture.LU));
			messages.add(new Message(4, new Utilisateur(2, "Monique"), "Coucou à toi", df.parse("26/05/2017 15:24:51"), StatutDeLecture.RECU, StatutDeLecture.LU));
			messages.add(new Message(4, new Utilisateur(1, "Michel"), "Merci (en retard)", df.parse("02/06/2017 01:17:18"), StatutDeLecture.RECU, StatutDeLecture.RECU));
			
			g.addTicketConnu(new Ticket(5, "mon ticket", messages, new Date(), new Date()));
		}
		catch (ParseException e){
			e.printStackTrace();
		}
		// Parcours correct
		System.out.println("\n## Proprement");
		for (Groupe gr: groupes) {
			System.out.println(gr + " " + gr.getTicketsConnus().size());
			for (Ticket t: gr.getTicketsConnus()) {
				System.out.println(t + " complet: " + t.estComplet());
				if (t.estComplet())
					for (Message m: t.getMessages())
						System.out.println(m);
			}
		}		
		
		// Parcours plein d'erreurs
		System.out.println("\n## Salement");
		for (Groupe gr: groupes) {
			System.out.println(gr);
			for (Ticket t: gr.getTicketsConnus()) {
				System.out.println(t);
				try {
					for (Message m: t.getMessages())
						System.out.println(m);
				}
				catch (LacunaireException e) {
					System.out.println(e);
				}
			}
		}
			
	}

}
