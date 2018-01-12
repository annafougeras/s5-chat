/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import modele.Message;
import modele.StatutDeLecture;
import modele.Utilisateur;

/**
 *
 * @author Vincent Fougeras
 */
public class MessageListCellRenderer extends JLabel implements ListCellRenderer<Message> {
    
    private DateFormat dateFormat = new SimpleDateFormat("dd/mm");
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Message> jlist, Message message, int index, boolean isSelected, boolean hasFocus) {
        
        // Allow multiline content
        JTextArea renderer = new JTextArea(1,10);
        renderer.setLineWrap(true);
        
        renderer.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        if(index == 0 && message.getIdentifiantNumeriqueUnique() == 0 && message.getEmetteur() == null){
            // Aucun message
            renderer.setText(message.getTexte());
        }
        else {
            if(message.getEmetteur() == null || message.getStatuts() == null || message.getDateEmission() == null){
                // Informations manquantes : impossible de rendre le message
                renderer.setText(message.getTexte() + " [MessageListCellRenderer : infos manquantes]");
            }
            else {
                // Texte du message
                Utilisateur user = message.getEmetteur();
                renderer.setText("[" + user.getPrenom() + " " + user.getNom() + "] (" 
                        + dateFormat.format(message.getDateEmission()) + ")\n" 
                        + message.getTexte());

                // Couleur du message
                StatutDeLecture statut = StatutDeLecture.LU;
                for(StatutDeLecture currentStatut : message.getStatuts().values()){
                    if(currentStatut.compareTo(statut) < 0){
                        statut = currentStatut;
                    }
                }
                switch (statut){
                    case NON_ENVOYE : renderer.setBackground(Color.LIGHT_GRAY);
                                      break;
                    case ENVOYE : renderer.setBackground(Color.RED);
                                  break;
                    case RECU : renderer.setBackground(Color.ORANGE);
                                break;
                    case LU : renderer.setBackground(Color.GREEN);
                }
            }
            
        }
        
        return renderer;
    }
    
}
