package vue;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class BasePanel extends JPanel {
	protected JDialog getParentDialog(){
        return ((JDialog)this.getParent().getParent().getParent().getParent());
    }
    
	protected void closeParentDialog(){
        getParentDialog().dispose();
    }
}
