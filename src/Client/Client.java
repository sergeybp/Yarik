package Client;

import Network.Messages.InitMessages.MessageAskForServer;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.Network;
import Network.StandartQuad;
import javafx.util.Pair;
import org.json.simple.JSONObject;
import abstractttt.AbstractServer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sergeybp on 27.10.16.
 */

//This is an example implementation of Client for Yarik.
public class Client extends AbstractServer {

    static StandartQuad myQuad = new StandartQuad("CLIE1", "0.0.0.0", 4568, Network.CLIENT.name());

    Client() {
        super(myQuad);
    }

    @Override
    public void handleReceive(JSONObject object) {
        YarikMessage gotMessage = new MessageUnknown();
        try {
            gotMessage = gotMessage.decode(object);
        } catch (Exception e) {
            // if JSON is not full
            e.printStackTrace();
        }

        // Show message
        String toShow = "";
        toShow += "type = " + gotMessage.getMessageType() + "\n";
        ArrayList<Pair<String, String>> gotContent = gotMessage.getMessageContent();
        for (Pair<String, String> pair : gotContent) {
            toShow += pair.getKey() + " -- " + pair.getValue() + "\n";
        }
        System.out.print(toShow);
    }


    public static void main(String[] args) {
        new Client().run();
    }

    void run() {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StandartQuad initQuad = getInitFromConfig();
        sendAskForServer(initQuad);

    }


    public StandartQuad getInitFromConfig() {
        // TODO Now we actually don't have configs :(
        return new StandartQuad("TESTINIT", "0.0.0.0", 8765, Network.INIT.name());
    }

    public void sendAskForServer(StandartQuad init) {
        YarikMessage message = new MessageAskForServer();
        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        try {
            message.setFieldsContent(content);
        } catch (Exception e) {
            //here we can get exception if content not full
            e.printStackTrace();
        }
        JSONObject object = message.encode();

        try {
            sendJSON(init.ip, init.port, object);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
