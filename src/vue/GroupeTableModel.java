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
public class GroupeTableModel extends AbstractTableModel
{
    
    private ICtrlAdmin ctrlAdmin;
    private List<Groupe> groupes;
    
    public GroupeTableModel(ICtrlAdmin ctrlAdmin){
        this.ctrlAdmin = ctrlAdmin;
        this.groupes = new ArrayList<>(ctrlAdmin.getGroupes());
    }

    @Override
    public int getRowCount() {
        return groupes.size();
    }

    @Override
    public int getColumnCount() {
        return 3; // Group name, Details, Retirer
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object returnedObject;
        switch (col) {
            case 0 :
                returnedObject = groupes.get(row);
                break;
            case 1 :
                returnedObject = "Details";
                break;
            default :
                returnedObject = "Retirer";
        }
        return returnedObject;
    }
    
    public void update(){
        this.groupes.clear();
        this.groupes.addAll(this.ctrlAdmin.getGroupes());
        fireTableDataChanged();
    }
    
}
