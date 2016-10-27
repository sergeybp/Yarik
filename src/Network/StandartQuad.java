package Network;

/**
 * Created by sergeybp on 27.10.16.
 */
public class StandartQuad {

    public String globalName;
    public String ip;
    public String type;
    public int port;

    public StandartQuad(String globalName, String ip, int port, String type) {
        this.globalName = globalName;
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public String toString() {
        return globalName + "$" + ip + "$" + port + "$" + type;
    }


}
