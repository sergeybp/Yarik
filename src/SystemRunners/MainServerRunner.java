package SystemRunners;

import MainServer.MainServer;
import Network.StandartQuad;

/**
 * Created by sergeybp on 21.11.16.
 */
public class MainServerRunner {

    public static void main(String[] args){
        MainServer m = new MainServer(new StandartQuad("SERVER1","0.0.0.0",8777,"MAIN"));
        m.start();
    }

}
