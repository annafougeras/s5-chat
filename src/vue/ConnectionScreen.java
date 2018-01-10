/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.ICtrlVue;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Vincent Fougeras
 */
public class ConnectionScreen extends BaseScreen {
        
    /**
     * Creates new form ConnectionScreen
     */
    public ConnectionScreen(ICtrlVue ctrlVue) {
        super(ctrlVue);
        initComponents();
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

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        identifiantTextField = new javax.swing.JTextField();
        connectionButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        passwordPasswordField = new javax.swing.JPasswordField();
        connectionStatus = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Environment.APP_NAME);
        setPreferredSize(new java.awt.Dimension(600, 400));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Connexion");
        jLabel1.setAlignmentX(0.5F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 50, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        identifiantTextField.setToolTipText("Identifiant");
        identifiantTextField.setName(""); // NOI18N
        identifiantTextField.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 0);
        getContentPane().add(identifiantTextField, gridBagConstraints);

        connectionButton.setLabel("Se connecter");
        connectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 0, 0);
        getContentPane().add(connectionButton, gridBagConstraints);

        jLabel2.setText("Identifiant");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setText("Mot de passe");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        getContentPane().add(jLabel3, gridBagConstraints);

        passwordPasswordField.setPreferredSize(new java.awt.Dimension(150, 30));
        passwordPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordPasswordFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 0);
        getContentPane().add(passwordPasswordField, gridBagConstraints);

        connectionStatus.setForeground(new java.awt.Color(150, 0, 0));
        connectionStatus.setText("Connection ratée");
        connectionStatus.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        getContentPane().add(connectionStatus, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passwordPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordPasswordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordPasswordFieldActionPerformed

    private void connectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionButtonActionPerformed
        connectionStatus.setVisible(false);
        String identifiant = identifiantTextField.getText();
        //MARCHE PAS String password = Arrays.toString(passwordPasswordField.getPassword());
        String password = "";
        for (char c: passwordPasswordField.getPassword())
        	password += c;
        System.out.println(password);
        if(! this.ctrlVue.connecter(identifiant, password)){
            connectionStatus.setVisible(true);
        }
    }//GEN-LAST:event_connectionButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectionButton;
    private javax.swing.JLabel connectionStatus;
    private javax.swing.JTextField identifiantTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPasswordField passwordPasswordField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object o1) {
        // Nothing to update ?
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
