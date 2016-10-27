package InitServer;

import Network.Messages.ClientMessages.MessagePublish;
import Network.Messages.InitMessages.MessageReturnServerAddress;
import Network.Messages.YarikMessage;
import Network.Network;
import Network.StandartQuad;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sergeybp on 27.10.16.
 */
public class AnswerToClient {

    Network network;
    String quadAddr;
    String serverToReturn;

    public AnswerToClient(Network network, String quadAddr, String serverToReturn){
        this.network = network;
        this.quadAddr = quadAddr;
        this.serverToReturn = serverToReturn;
    }

    public void run(){
        StandartQuad client =  fromString(quadAddr);
        YarikMessage message = new MessageReturnServerAddress();
        ArrayList<String> content = new ArrayList<>();
        content.add(serverToReturn);
        try {
            message.setFieldsContent(content);
        } catch (Exception e) {
            //here we can get exception if content not full
            e.printStackTrace();
        }
        // getting JSON from message to send
        JSONObject object = message.encode();
        try {
            network.sendJSON(client.ip,client.port,object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StandartQuad fromString (String s){
        String[] splits = s.split("\\$");
        return new StandartQuad(splits[0],splits[1],Integer.parseInt(splits[2]),splits[3]);
    }

}
