package abstractttt;

import Network.StandartQuad;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Created by sergeybp on 27.10.16.
 */
public abstract class AbstractServer {

    protected String globalName;
    protected String ip;
    protected int port;
    protected String TYPE;

    Server server;

    public AbstractServer(StandartQuad quad){
        this.globalName = quad.globalName;
        this.ip = quad.ip;
        this.port = quad.port;
        this.TYPE = quad.type;
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

    public abstract void handleReceive(JSONObject object);



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

    public String start() {
        try {
            startServer();
        } catch (IOException e) {
            return "FAILED :: " + e.toString();
        }
        return "SERVER STARTED :: gNAME=" + getGlobalName() + " , ip=" + getIp() + " , port=" + getPort() + " ;";
    }

    public String stop() {
        stopServer();
        return "SERVER STOPPED;";
    }

    public static void sendMSG(){

    }

}
