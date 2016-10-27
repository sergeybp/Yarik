package Client;

import Network.Messages.ClientMessages.MessagePublish;
import Network.Messages.InitMessages.MessageAskForServer;
import Network.Messages.YarikMessage;
import Network.Network;
import Network.StandartQuad;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sergeybp on 27.10.16.
 */

//This is an example implementation of Client for Yarik.
public class Client {

    Network network;
    StandartQuad myQuad = new StandartQuad("CLIENT1", "0.0.0.0", 4567, Network.CLIENT);

    public static void main(String[] args){
        new Client().run();
    }

    void run(){
        network = new Network(myQuad.globalName, myQuad.ip, myQuad.port, myQuad.type);
        try {
            network.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StandartQuad initQuad = getInitFromConfig();
        sendAskForServer(initQuad);

    }


    public StandartQuad getInitFromConfig(){
        // TODO Now we actually don't have configs :(
        return new StandartQuad("TESTINIT", "0.0.0.0", 8765, Network.INIT);
    }

    public void sendAskForServer(StandartQuad init){
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
            network.sendJSON(init.ip,init.port,object);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
