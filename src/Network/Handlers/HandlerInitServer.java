package Network.Handlers;

import InitServer.InitServer;
import InitServer.AnswerToClient;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageType;
import Network.Network;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by sergeybp on 27.10.16.
 */
public class HandlerInitServer extends AbstractHandler {

    Network network;

    public HandlerInitServer(Network network){
        this.network = network;
    }

    @Override
    public void handle(JSONObject object) {
        YarikMessage gotMessage = new MessageUnknown();
        try {
            gotMessage = gotMessage.decode(object);
        } catch (Exception e) {
            // if JSON is not full
            e.printStackTrace();
        }

        if(gotMessage.getMessageType().equals(YarikMessageType.ASKFORSERVER)){
            //TODO Really ask servers about load
            new AnswerToClient(network,gotMessage.getMessageContent().get(0).getValue(), "TESTSERVERTORETURN").run();
        }

    }

}
