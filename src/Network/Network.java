package Network;

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

    String globalName;
    String ip;
    int port;

    public Network(String globalName, String ip, int port){
        this.globalName = globalName;
        this.ip = ip;
        this.port = port;
    }


    public void startServer() throws IOException {
        Server server = new Server();
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
        Handler handler = new Handler();
        handler.handle(object);
    }

    public void sendJSON(String ip, int port, JSONObject object) throws IOException {
        Client client = new Client();
        client.start();
        Kryo kryo = client.getKryo();
        kryo.register(JSONObject.class);
        client.connect(5000, ip, port);
        client.sendTCP(object);
    }

}
