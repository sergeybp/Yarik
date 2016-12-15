package SystemRunners;

import Database.DatabaseServer;
import Network.Network;
import Network.StandardQuad;

/**
 * Created by sergeybp on 21.11.16.
 */
public class DBServerRunner {

    public static void main(String[] args){
        DatabaseServer d = new DatabaseServer(new StandardQuad("DB","0.0.0.0",8989, Network.DATABASE.name()));
        d.start();
    }
}
