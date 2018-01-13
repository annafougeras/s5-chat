package vue;

import javax.swing.AbstractListModel;
import modele.Message;
import java.util.List;

/**
 *
 * @author Vincent Fougeras
 */
public class MessageListModel extends AbstractListModel {
    
    private List<Message> messages;
    
    public MessageListModel(List<Message> messages){
        this.messages = messages;
    }

    @Override
    public int getSize() {
        if(messages.isEmpty()){
            return 1;
        } else {
            return messages.size();
        }            
        
    }

    @Override
    public Object getElementAt(int i) {
        if(messages.isEmpty()){
            return new Message(0, null, "Aucun message", null, null);
        } else {
            return messages.get(i);
        }
        
    }
    
}
