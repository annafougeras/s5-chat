/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.ICtrlAdmin;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import javax.swing.table.AbstractTableModel;
import modele.Groupe;

/**
 *
 * @author Vincent Fougeras
 */
public class UserOrGroupeTableModel<T> extends AbstractTableModel
{

    private List<T> usersOrGroupes;
    
    public UserOrGroupeTableModel(List<T> usersOrGroupes){
        this.usersOrGroupes = usersOrGroupes;
    }

    @Override
    public int getRowCount() {
        return usersOrGroupes.size();
    }

    @Override
    public int getColumnCount() {
        return 3; // User/Group name, Details, Retirer
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object returnedObject;
        switch (col) {
            case 0 :
                returnedObject = usersOrGroupes.get(row);
                break;
            case 1 :
                returnedObject = "Details";
                break;
            default :
                returnedObject = "Retirer";
        }
        return returnedObject;
    }    
}
