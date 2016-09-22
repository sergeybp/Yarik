package Network;

import Messages.MessageUnknown;
import Messages.YarikMessage;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by sergeybp on 22.09.16.
 */
public class HandlerMain {

    HandlerMain(){

    }

    public void handle(JSONObject object){

        //TODO
        // Now it's just showing message to system out.

        // Now getting message from JSON
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
