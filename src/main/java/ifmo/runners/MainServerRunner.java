package ifmo.runners;

import ifmo.server.main.MainServer;
import ifmo.network.StandardQuad;
/**
 * Created by sergeybp on 21.11.16.
 */
public class MainServerRunner {

    public static void main(String[] args){
        MainServer m = new MainServer(new StandardQuad("SERVER1","0.0.0.0",8777,"MAIN"));
        m.start();
    }

}
