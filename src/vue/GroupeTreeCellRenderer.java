package vue;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import modele.Groupe;
import modele.Ticket;

/**
 *
 * @author Vincent Fougeras
 */
public class GroupeTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    
        int nbNonLus = 0;
        
        if(value instanceof Groupe){
            Groupe groupe = (Groupe) value;
            
            for(Ticket t : groupe.getTicketsConnus()){
                nbNonLus += t.getNbMessagesNonLus();
            }
        }
        else if (value instanceof Ticket){
            Ticket ticket = (Ticket) value;
            nbNonLus = ticket.getNbMessagesNonLus();
        }
        
        if(nbNonLus > 0){
            setFont(new Font("Sans", Font.BOLD, 12));
            setText("(" + nbNonLus + ") " + value.toString());
        }
        else {
           setFont(new Font("Sans", Font.PLAIN, 12));
           setText(value.toString());
        }
                
        return this;
    }
    
}
