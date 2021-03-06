package ifmo.network;

/**
 * Created by sergeybp on 27.10.16.
 */
public class StandardQuad {

    public String globalName;
    public String ip;
    public int port;
    public String type;

    public StandardQuad(String globalName, String ip, int port, String type) {
        this.globalName = globalName;
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public StandardQuad(String quadString){
        String[] splits = quadString.split("\\$");
        this.globalName = splits[0];
        this.ip = splits[1];
        this.port = Integer.parseInt(splits[2]);
        this.type = splits[3];
    }

    public String toString() {
        return globalName + "$" + ip + "$" + port + "$" + type;
    }


}
