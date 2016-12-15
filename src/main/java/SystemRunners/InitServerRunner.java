package SystemRunners;

import InitServer.InitServer;
import Network.Network;
import Network.StandardQuad;
/**
 * Created by sergeybp on 21.11.16.
 */
public class InitServerRunner {

    public static void main(String[] args){
        InitServer i = new InitServer(new StandardQuad("TESTINIT", "0.0.0.0", 8765, Network.INIT.name()));
        i.start();
    }

}
