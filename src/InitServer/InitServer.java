package InitServer;

import Network.Messages.InitMessages.MessageReturnServerAddress;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.Messages.YarikMessageType;
import Network.StandartQuad;
import abstractttt.AbstractServer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sergeybp on 27.10.16.
 */
public class InitServer extends AbstractServer {

    public InitServer(StandartQuad quad){
        super(quad);
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

        if(gotMessage.getMessageType().equals(YarikMessageType.ASKFORSERVER)){
            //TODO Really ask servers about load
            StandartQuad quad = new StandartQuad(gotMessage.getMessageContent().get(0).getValue());
            YarikMessage message = new MessageReturnServerAddress();
            ArrayList<String> content = new ArrayList<>();
            content.add("TEST_SERVER_TO_RETURN");
            try {
                message.setFieldsContent(content);
            } catch (Exception e) {
                //here we can get exception if content not full
                e.printStackTrace();
            }
            // getting JSON from message to send
            JSONObject object1 = message.encode();
            try {
                sendJSON(quad.ip,quad.port,object1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
