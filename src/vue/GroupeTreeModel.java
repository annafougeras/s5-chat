/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import modele.Ticket;
import modele.Groupe;

/**
 *
 * @author Vincent Fougeras
 */
public class GroupeTreeModel implements TreeModel {
    
    List<Groupe> groupes;
    
    public GroupeTreeModel(NavigableSet<Groupe> groupes){
        this.groupes = new ArrayList<>(groupes);
    }

    @Override
    public Object getRoot() {
        return "/";
    }

    @Override
    public Object getChild(Object o, int i) {
        if (o instanceof String && ((String)o).equals("/") ){
            return groupes.get(i);
        }
        else if (o instanceof Groupe){
            // getTicketConnus() renvoie un NavigableSet
            // toArray() renvoie un Object[], mais toArray(T[]) renvoie un T[]
            return ((Groupe)o).getTicketsConnus().toArray(new Ticket[0])[i];
        }
        else {
            return null;
        }
    }

    @Override
    public int getChildCount(Object o) {
        if (o instanceof String && ((String)o).equals("/") ){
            return groupes.size();
        }
        else if (o instanceof Groupe){
            return ((Groupe)o).getTicketsConnus().size();
        }
        else {
            return 0;
        }
    }

    @Override
    public boolean isLeaf(Object o) {
        return o instanceof Ticket;
    }

    @Override
    public void valueForPathChanged(TreePath tp, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIndexOfChild(Object o, Object o1) {
        if(o == null || o1 == null){
            return -1;
        }
        
        if (o instanceof String && ((String)o).equals("/") ){
            return groupes.indexOf(o1);
        }
        else if (o instanceof Groupe){
            // getTicketConnus() renvoie un NavigableSet
            // toArray() renvoie un Object[], mais toArray(T[]) renvoie un T[]
            List<Ticket> tickets = new ArrayList<>(((Groupe)o).getTicketsConnus());
            return tickets.indexOf(o1);
        }
        else {
            return -1;
        }
    }

    @Override
    public void addTreeModelListener(TreeModelListener tl) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener tl) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
