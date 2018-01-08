/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;

import javax.swing.JDialog;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import controleur.CtrlVue;
import controleur.ICtrlVue;
import java.util.ArrayList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modele.Groupe;
import modele.Message;
import modele.Ticket;

/**
 *
 * @author Vincent Fougeras
 */
public class MainScreen extends BaseScreen {

    /**
     * Creates new form MainScreen
     */
    public MainScreen(ICtrlVue ctrlVue) {
        super(ctrlVue);
        initComponents();
    }
    
    /**
    * Scrolls a {@code scrollPane} to its bottom.
    * 
    * @param scrollPane
    *            the scrollPane that we want to scroll all the way down
    * 
    */
   private void scrollDown(JScrollPane scrollPane) {
    JScrollBar verticalBar = scrollPane.getVerticalScrollBar();

    int currentScrollValue = verticalBar.getValue();
    int previousScrollValue = -1;

    while (currentScrollValue != previousScrollValue) {
        // Scroll down a bit
        int downDirection = 1;
        int amountToScroll = verticalBar.getUnitIncrement(downDirection);
        verticalBar.setValue(currentScrollValue + amountToScroll);

        previousScrollValue = currentScrollValue;
        currentScrollValue = verticalBar.getValue();
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        envoyerButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageList = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ticketTree = new javax.swing.JTree(new GroupeTreeModel(this.ctrlVue.getModel()));
        addTicketButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        deconnectionMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Environment.APP_NAME);
        setPreferredSize(new java.awt.Dimension(800, 600));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(500, 23));
        jPanel2.setLayout(new java.awt.BorderLayout(5, 0));

        envoyerButton.setText("Envoyer");
        jPanel2.add(envoyerButton, java.awt.BorderLayout.LINE_END);

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(2);
        jScrollPane3.setViewportView(jTextArea1);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        messageList.setModel(new MessageListModel(new ArrayList<Message>()));
        messageList.setCellRenderer(new MessageListCellRenderer());

        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        messageList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                openMessageDetailsModal();
            }
        });
        jScrollPane2.setViewportView(messageList);

        scrollDown(jScrollPane2);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 23));

        ticketTree.setMaximumSize(null);
        ticketTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                ticketTreeValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(ticketTree);
        ticketTree.setRootVisible(false);
        ticketTree.setShowsRootHandles(true);
        ticketTree.setCellRenderer(new GroupeTreeCellRenderer());

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        addTicketButton.setText("+ Ajouter un ticket");
        addTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTicketButtonActionPerformed(evt);
            }
        });
        jPanel3.add(addTicketButton, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setLeftComponent(jPanel3);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Fichier");

        deconnectionMenuItem.setText("Déconnection");
        deconnectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deconnectionMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(deconnectionMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTicketButtonActionPerformed
        JDialog addDialog = new JDialog(this, "Créer un ticket", true);
        final AddPanel addPanel;
        addPanel = new AddPanel(this.ctrlVue);
        addDialog.add(addPanel);
        addDialog.addWindowListener(new WindowAdapter() 
        {
          public void windowClosed(WindowEvent e)
          {
            ((CtrlVue)ctrlVue).deleteObserver(addPanel);
          }

          public void windowClosing(WindowEvent e)
          {
            ((CtrlVue)ctrlVue).deleteObserver(addPanel);
          }
        });
        addDialog.pack();
        addDialog.setVisible(true);
    }//GEN-LAST:event_addTicketButtonActionPerformed

    private void deconnectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnectionMenuItemActionPerformed
        this.ctrlVue.deconnecter();
    }//GEN-LAST:event_deconnectionMenuItemActionPerformed

    /**
     * Agit en fonction de l'item sélectionné dans le JTree (déroule le groupe, ou ouvre le ticket)
     * @param evt 
     */ 
    private void ticketTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {                                        
        if(ticketTree.getLastSelectedPathComponent() instanceof Groupe){
            ticketTree.expandPath(evt.getNewLeadSelectionPath()); /* Ne marche pas tout le temps mais on a le double-click + le "plus" qui marchent tout le temps */
        } else if (ticketTree.getLastSelectedPathComponent() instanceof Ticket){
            Ticket ticket = (Ticket) ticketTree.getLastSelectedPathComponent();
            
            /* 
                Ouvrir le ticket dans le panel de droite 
            */
            this.ctrlVue.getRemoteMessages(ticket); // Demander au controleur de mettre a jour le ticket
            
            // J'utilisais cette fonction auparavant pour mettre à jour les messages mais 
            // c'est surement mieux de faire comme tu as fait (en utilisant des CtrlVue.Notification)
            //this.updateMessageList(ticket); // Afficher les messages du ticket
        }
    }
    
    /**
     * Retourne le ticket actuellement selectionne dans le JTree
     * @return le ticket actuellement selectionne ou null
     */
    private Ticket getSelectedTicket(){
        if (ticketTree.getLastSelectedPathComponent() instanceof Ticket){
            Ticket ticket = (Ticket) ticketTree.getLastSelectedPathComponent();
            return ticket;
        }
        return null;
    }
    
    private void openMessageDetailsModal(){
        // Récupérer la sélection
        Message message = messageList.getSelectedValue();
        
        if(message != null){
            // Ouvrir le modal
            JDialog detailsDialog = new JDialog(this, "Détails du message", true);
            MessageDetailsPanel detailsPanel = new MessageDetailsPanel(message);
            detailsDialog.add(detailsPanel);

            detailsDialog.pack();
            detailsDialog.setVisible(true);
        }        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTicketButton;
    private javax.swing.JMenuItem deconnectionMenuItem;
    private javax.swing.JButton envoyerButton;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JList<Message> messageList;
    private javax.swing.JTree ticketTree;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object o1) {
        // Unable to know which object has been updated -> update everything
        // Update ticketTree, messageList (with currently selected ticket)
    	
       /*
        NavigableSet<Groupe> groupes = this.ctrlVue.getModel();
        
        this.updateTicketTree(groupes);
        
        Ticket selectedTicket = this.getSelectedTicket();
        
        this.updateMessageList(selectedTicket);*/
                
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    	
    	if (o1 instanceof CtrlVue.Notification){
    		CtrlVue.Notification notification = (CtrlVue.Notification) o1;
    		switch (notification){
    		case UPDATE_JTREE:
    			ticketTree.setModel(new GroupeTreeModel(ctrlVue.getModel()));
    			ticketTree.updateUI();
    			break;
    		default:
    			System.err.println("Notification non traitée : " + notification);
    			
    		}
    	}
    	else
    		System.err.println("Notification non traitée : " + o1);
    	
    }
    
}
