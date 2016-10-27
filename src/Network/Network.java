package Network;

import Network.Handlers.ClientHandler;
import Network.Handlers.HandlerInitServer;
import Network.Handlers.HandlerMain;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Created by sergeybp on 22.09.16.
 */
public class Network {

    public static String INIT = "INIT";
    public static String MAIN = "MAIN";
    public static String CLIENT = "CLIENT";

    String globalName;
    String ip;
    int port;
    String handlerName;

    private Server server;

    public Network(String globalName, String ip, int port, String handlerName){
        this.globalName = globalName;
        this.ip = ip;
        this.port = port;
        this.handlerName = handlerName;
    }


    public void stopServer(){
        server.close();
    }

    public void startServer() throws IOException {
        server = new Server();
        server.start();
        server.bind(port);
        Kryo kryo = server.getKryo();
        kryo.register(JSONObject.class);
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof JSONObject) {
                    handleReceive((JSONObject) object);
                }
            }
        });
    }

    public void handleReceive(JSONObject object){
        if(handlerName.equals(MAIN)) {
            HandlerMain handler = new HandlerMain();
            handler.handle(object);
        }
        if(handlerName.equals(INIT)) {
            HandlerInitServer handler = new HandlerInitServer(this);
            handler.handle(object);
        }
        if(handlerName.equals(CLIENT)) {
            ClientHandler handler = new ClientHandler();
            handler.handle(object);
        }
    }

    public void sendJSON(String ip, int port, JSONObject object) throws IOException {
        Client client = new Client();
        client.start();
        Kryo kryo = client.getKryo();
        kryo.register(JSONObject.class);
        client.connect(5000, ip, port);
        client.sendTCP(object);
        client.close();
    }

    public String getGlobalName(){
        return globalName;
    }

    public String getIp(){
        return ip;
    }

    public int getPort(){
        return port;
    }

}
