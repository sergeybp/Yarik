package InitServer;

import Network.Network;

import java.io.IOException;

/**
 * Created by sergeybp on 27.10.16.
 */
public class InitServer {

    private Network network;

    public InitServer(String globalName, String ip, int port) {
        this.network = new Network(globalName, ip, port, Network.INIT);
    }

    public String start() {
        try {
            network.startServer();
        } catch (IOException e) {
            return "FAILED :: " + e.toString();
        }
        return "SERVER STARTED :: gNAME=" + network.getGlobalName() + " , ip=" + network.getIp() + " , port=" + network.getPort() + " ;";
    }

    public String stop() {
        network.stopServer();
        return "SERVER STOPPED;";
    }

}
