package Network.Handlers;

import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by sergeybp on 27.10.16.
 */
public class ClientHandler extends AbstractHandler {
    @Override
    public void handle(JSONObject object) {
        YarikMessage gotMessage = new MessageUnknown();
        try {
            gotMessage = gotMessage.decode(object);
        } catch (Exception e) {
            // if JSON is not full
            e.printStackTrace();
        }

        // Show message
        String toShow = "";
        toShow+="type = "+ gotMessage.getMessageType()+ "\n";
        ArrayList<Pair<String,String>> gotContent = gotMessage.getMessageContent();
        for(Pair<String,String> pair : gotContent){
            toShow+=pair.getKey()+" -- "+pair.getValue()+"\n";
        }
        System.out.print(toShow);
    }
}
