/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.CtrlAdmin;
import controleur.CtrlVue;
import controleur.ICtrlAdmin;
import controleur.ICtrlVue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modele.Groupe;
import modele.Utilisateur;

/**
 *
 * @author Vincent Fougeras
 */
public class DetailsUserPanel extends javax.swing.JPanel implements Observer {

    private ICtrlAdmin ctrlAdmin;
    private Utilisateur user;
    private NavigableSet<Groupe> groupes;
    
    /**
     * Creates new form AddPanel
     */
    public DetailsUserPanel(ICtrlAdmin ctrlAdmin, Utilisateur user) {
        this.ctrlAdmin = ctrlAdmin;
        this.user = user;
        this.groupes = ctrlAdmin.getGroupes(user);
        
        // Il faut s'ajouter soi même au ctrlAdmin pour être notifié
        ((CtrlAdmin)this.ctrlAdmin).addObserver(this);
        
        initComponents();
        
        // Cette fonction fait des trucs que si ils étaient faits par netbeans 
        // ils seraient dans initComponents(). En bon français.
        initComponentsBis();
        
        userNameLabel.setText("Détails de l'utilisateur " + user.getNom() + " " + user.getPrenom());
    }
    
    private JDialog getParentDialog(){
        return ((JDialog)this.getParent().getParent().getParent().getParent());
    }
    
    private void closeParentDialog(){
        getParentDialog().dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        groupTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        userNameLabel = new javax.swing.JLabel();
        supprimerUserButton = new javax.swing.JButton();
        modifierUserButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        addGroupButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 0), new java.awt.Dimension(32767, 0));
        annulerButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(600, 400));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        groupTable.setModel(new UserOrGroupeTableModel<Groupe>(new ArrayList<Groupe>(this.groupes)));
        groupTable.setCellSelectionEnabled(true);
        groupTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupTable.addMouseListener(new GroupTableSelectionListener());
        jScrollPane1.setViewportView(groupTable);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        userNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        userNameLabel.setText("Détails de l'utilisateur ");
        userNameLabel.setAlignmentX(0.5F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 0);
        jPanel3.add(userNameLabel, gridBagConstraints);

        supprimerUserButton.setText("Supprimer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel3.add(supprimerUserButton, gridBagConstraints);

        modifierUserButton.setText("Modifier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        jPanel3.add(modifierUserButton, gridBagConstraints);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        addGroupButton.setText("Ajouter un groupe");
        addGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGroupButtonActionPerformed(evt);
            }
        });
        jPanel4.add(addGroupButton);

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jPanel2.add(filler1);

        annulerButton.setText("Retour");
        annulerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerButtonActionPerformed(evt);
            }
        });
        jPanel2.add(annulerButton);

        add(jPanel2, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void annulerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerButtonActionPerformed
        this.closeParentDialog();
    }//GEN-LAST:event_annulerButtonActionPerformed

    private void addGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGroupButtonActionPerformed
        JDialog addDialog = new JDialog(getParentDialog(), "Ajouter un groupe", true);
        // TODO addGroupToUserPanel
        //AddGroupPanel addGroupPanel = new AddGroupPanel(this.ctrlAdmin);
        //addDialog.add(addGroupPanel);
        //addDialog.pack();
        //addDialog.setVisible(true);
    }//GEN-LAST:event_addGroupButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addGroupButton;
    private javax.swing.JButton annulerButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JTable groupTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton modifierUserButton;
    private javax.swing.JButton supprimerUserButton;
    private javax.swing.JLabel userNameLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object o1) {
        // Mettre à jour la liste des groupes existants
        this.groupes = ctrlAdmin.getGroupes();
        // Changer la table de modèle
        groupTable.setModel(new UserOrGroupeTableModel<>(new ArrayList<>(this.groupes)));
    }
    
    private class GroupTableSelectionListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                int row = groupTable.getSelectedRow();
                int col = groupTable.getSelectedColumn();
                if(row >= 0 && col >= 0){
                    Groupe groupe = (new ArrayList<>(groupes)).get(row);
                    switch (col){
                        case 0 :
                        case 1 :
                            // Fermer le JDialog, et ouvrir le JDialog de ce groupe
                            closeParentDialog();
                            showGroupDetails(groupe);
                            break;
                        default :
                            // Retirer l'utilisateur de ce groupe
                            ctrlAdmin.removeUtilisateurFromGroupe(user, groupe);
                    }
                }
            }
        }        
    }
    
    private void showGroupDetails(Groupe groupe) {
    	JDialog detailsGroupeDialog = new JDialog(this.getParentDialog(), "Détails du groupe", true);
        DetailsGroupePanel detailsGroupePanel = new DetailsGroupePanel(this.ctrlAdmin, groupe);
        detailsGroupeDialog.add(detailsGroupePanel);
        detailsGroupeDialog.pack();
        detailsGroupeDialog.setVisible(true);
    }
    
     /**
	 * Celle-là c'est moi qui l'a fait on peut y toucher sans problème...
	 */
	private void initComponentsBis() {
		supprimerUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	int dialogResult = JOptionPane.showConfirmDialog (
            			null, 
            			"Supprimer l'utilisateur ?",
            			"Confirmation",
            			JOptionPane.YES_NO_OPTION
            			);
            	if(dialogResult == JOptionPane.YES_OPTION){
	            	ctrlAdmin.supprimerUtilisateur(user);
	            	closeParentDialog();
            	}
            }
        });
	/*	modifierUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String nouveau_nom = JOptionPane.showInputDialog(
            	        null, 
            	        "Donner un nouveau nom à l'utilisateur : ", 
            	        "Nouveau nom", 
            	        JOptionPane.QUESTION_MESSAGE
            	    );
            	if (nouveau_nom != null)
            		if (nouveau_nom.length() > 1)
            			ctrlAdmin.insererUtilisateur(new Utilisateur(user.getIdentifiantNumeriqueUnique(), nouveau_nom, nouveau_prenom));
            	
            }
        });*/
		addGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
            	NavigableSet<Groupe> groupes = ctrlAdmin.getGroupes();
            	Groupe[] tab = new Groupe[groupes.size()];
            	int i = 0;
            	for (Groupe g : groupes)
            		tab[i++] = g;
            	
            	Groupe g = (Groupe) JOptionPane.showInputDialog(
            			null, 
            			"Quel groupe ?",
            			"L'utilisateur rejoint un groupe",
            			JOptionPane.QUESTION_MESSAGE, 
            			null, 
            			tab,
            			tab[0]
            		);
                
                if(g != null){
                    System.out.println(g);
                    ctrlAdmin.addUtilisateurToGroupe(user, g);
                }
            	
            }
        });
	}
}
