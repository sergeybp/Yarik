package SystemRunners;

import InitServer.InitServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by sergeybp on 27.10.16.
 */
public class PrettySystemRunner {

    static ArrayList<InitServer> inits = new ArrayList<>();

    public static void main(String[] args){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String line = reader.readLine();
                        if (line == null || line.equals("exit")) {
                            System.exit(0);
                            return;
                        }
                        String[] slices = line.trim().split(" +");
                        if (slices[0].equals("stop") && slices[1].equals("INIT")) {
                            int id = Integer.parseInt(slices[2]);
                            System.out.println(inits.get(id).stop());
                        } else if (slices[0].equals("start") && slices[1].equals("INIT")) {
                            String gName = slices[2];
                            String ip = slices[3];
                            int port = Integer.parseInt(slices[4]);
                            InitServer server = new InitServer(gName,ip,port);
                            inits.add(server);
                            System.out.println(server.start());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
