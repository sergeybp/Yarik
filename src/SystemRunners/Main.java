package SystemRunners;

import Network.Messages.*;
import Network.Messages.ClientMessages.MessagePublish;
import Network.Network;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sergeybp on 22.09.16.
 */
public class Main {

    public static void main(String[] args) {

        //Creating message
        YarikMessage message = new MessagePublish();
        ArrayList<String> content = new ArrayList<>();
        content.add("message text");
        content.add("message tags");
        content.add("message feedback");
        try {
            message.setFieldsContent(content);
        } catch (Exception e) {
            //here we can get exception if content not full
            e.printStackTrace();
        }
        // getting JSON from message to send
        JSONObject object = message.encode();


        // starting server. HandlerMain will handle all incoming messages.
        Network network = new Network("Main", "0.0.0.0", 4444, Network.MAIN);
        try {
            network.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send this JSON
        try {
            network.sendJSON("0.0.0.0", 4444, object);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
