package vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import controleur.CtrlAdmin;
import controleur.ICtrlAdmin;
import modele.Groupe;
import modele.Utilisateur;

/**
 *
 * @author Vincent Fougeras
 */
@SuppressWarnings("serial")
public class DetailsGroupePanel extends BasePanel implements Observer {

    private ICtrlAdmin ctrlAdmin;
    private Groupe groupe;
    private NavigableSet<Utilisateur> users;
    
    /**
     * Creates new form AddPanel
     */
    public DetailsGroupePanel(ICtrlAdmin ctrlAdmin, Groupe groupe) {
        this.ctrlAdmin = ctrlAdmin;
        this.groupe = groupe;
        this.users = ctrlAdmin.getUtilisateurs(groupe);
        
        // Il faut s'ajouter soi même au ctrlAdmin pour être notifié
        ((CtrlAdmin)this.ctrlAdmin).addObserver(this);
        
        initComponents();
        initComponentsBis();

        groupeNameLabel.setText("Détails du groupe " + groupe.getNom());
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
        userTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        groupeNameLabel = new javax.swing.JLabel();
        supprimerGroupeButton = new javax.swing.JButton();
        modifierGroupeButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        addUserButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 0), new java.awt.Dimension(32767, 0));
        annulerButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(600, 400));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        userTable.setModel(new UserOrGroupeTableModel<Utilisateur>(new ArrayList<Utilisateur>(this.users)));
        userTable.setCellSelectionEnabled(true);
        userTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.addMouseListener(new UserTableSelectionListener());
        jScrollPane1.setViewportView(userTable);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        groupeNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        groupeNameLabel.setText("Détails du groupe");
        groupeNameLabel.setAlignmentX(0.5F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 0);
        jPanel3.add(groupeNameLabel, gridBagConstraints);

        supprimerGroupeButton.setText("Supprimer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel3.add(supprimerGroupeButton, gridBagConstraints);

        modifierGroupeButton.setText("Modifier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        jPanel3.add(modifierGroupeButton, gridBagConstraints);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        addUserButton.setText("Ajouter un utilisateur");
        addUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserButtonActionPerformed(evt);
            }
        });
        jPanel4.add(addUserButton);

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


    /**
	 *  Autres initialisations de composants (non modifiés par Netbeans)
	 */
	private void initComponentsBis() {
		supprimerGroupeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	int dialogResult = JOptionPane.showConfirmDialog (
            			null, 
            			"Supprimer le groupe ?",
            			"Confirmation",
            			JOptionPane.YES_NO_OPTION
            			);
            	if(dialogResult == JOptionPane.YES_OPTION){
	            	ctrlAdmin.supprimerGroupe(groupe);
	            	closeParentDialog();
            	}
            }
        });
		modifierGroupeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String nouveau_nom = JOptionPane.showInputDialog(
            	        null, 
            	        "Donner un nouveau nom au groupe : ", 
            	        "Nouveau nom", 
            	        JOptionPane.QUESTION_MESSAGE
            	    );
            	if (nouveau_nom != null){
            		if (nouveau_nom.length() > 1){
            			ctrlAdmin.insererGroupe(new Groupe(
            					groupe.getIdentifiantNumeriqueUnique(), 
            					nouveau_nom
            					));            			
            		}            		
            	}
            }
        });
	}
    private void annulerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerButtonActionPerformed
        this.closeParentDialog();
    }//GEN-LAST:event_annulerButtonActionPerformed

    private void addUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserButtonActionPerformed
    	NavigableSet<Utilisateur> utilisateurs = ctrlAdmin.getUtilisateurs();
    	Utilisateur[] tab = new Utilisateur[utilisateurs.size()];
    	int i = 0;
    	for (Utilisateur user: utilisateurs)
    		tab[i++] = user;
    	
    	Utilisateur user = (Utilisateur) JOptionPane.showInputDialog(
    			null, 
    			"Quel utilisateur ?",
    			"Un utilisateur rejoint le groupe",
    			JOptionPane.QUESTION_MESSAGE, 
    			null, 
    			tab,
    			tab[0]
    		);
        
        if(user != null){
            System.out.println(user);
            ctrlAdmin.addUtilisateurToGroupe(user, groupe);
        }
    }//GEN-LAST:event_addUserButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addUserButton;
    private javax.swing.JButton annulerButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel groupeNameLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton modifierGroupeButton;
    private javax.swing.JButton supprimerGroupeButton;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object o1) {
        // Mettre à jour la liste des groupes existants
        this.users = ctrlAdmin.getUtilisateurs(groupe);
        // Changer la table de modèle
        userTable.setModel(new UserOrGroupeTableModel<>(new ArrayList<>(this.users)));
    }
    
    private class UserTableSelectionListener extends MouseAdapter {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                int row = userTable.getSelectedRow();
                int col = userTable.getSelectedColumn();
                
                if(row >= 0 && col >= 0){
                    Utilisateur user = (new ArrayList<>(users)).get(row);
                    
                    switch (col){
                        case 0 :
                        case 1 :
                            // Fermer le JDialog, et ouvrir le JDialog de cet user
                            closeParentDialog();
                            showUserDetails(user);
                            break;
                        default :
                            // Retirer l'utilisateur de ce groupe
                            ctrlAdmin.removeUtilisateurFromGroupe(user, groupe);
                    }
                }
            }
        }      
    }
    
     private void showUserDetails(Utilisateur user) {
    	JDialog detailsUserDialog = new JDialog(this.getParentDialog(), "Détails de l'utilisateur", true);
        DetailsUserPanel detailsUserPanel = new DetailsUserPanel(this.ctrlAdmin, user);
        detailsUserDialog.add(detailsUserPanel);
        detailsUserDialog.pack();
        detailsUserDialog.setVisible(true);
    }
}
