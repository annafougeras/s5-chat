/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.table.AbstractTableModel;
import modele.Message;
import modele.StatutDeLecture;
import modele.Utilisateur;

/**
 *
 * @author Vincent Fougeras
 */
public class StatusTableModel extends AbstractTableModel
{    
    private List<Utilisateur> users;
    private List<StatutDeLecture> statuts;
    
    public StatusTableModel(Message message){
        this.users = new ArrayList<>();
        this.statuts = new ArrayList<>();
        for(Entry<Utilisateur, StatutDeLecture> entry : message.getStatuts().entrySet()){
            users.add(entry.getKey());
            statuts.add(entry.getValue());
        }
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return 2; // User, Statut
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object returnedObject;
        switch (col) {
            case 0 : // colonnes des utilisateurs
                returnedObject = users.get(row);
                break;
            default : // colonne des statuts
                returnedObject = statuts.get(row);
        }
        return returnedObject;
    }
    
    @Override
    public String getColumnName(int col) {
        String name;
        switch (col){
            case 0 : name = "Utilisateur";
                     break;
            default : name = "Statut";
                      break;
        }
        return name;
    }
}
