package SystemRunners;

import InitServer.InitServer;
import MainServer.MainServer;
import Network.Network;
import Network.StandartQuad;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by sergeybp on 27.10.16.
 */
public class PrettySystemRunner {

    static ArrayList<InitServer> inits = new ArrayList<>();
    static ArrayList<StandartQuad> servers = new ArrayList<StandartQuad>();

    public static void main(String[] args){

        getConfig();
        for(StandartQuad tmp : servers){
            MainServer server = new MainServer(tmp);
            System.out.println(server.start());
        }

        InitServer init = new InitServer(new StandartQuad("aaa", "0.0.0.0", 8765, Network.INIT.name()));
        System.out.println(init.start());
       /* BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
                            StandartQuad quad = new StandartQuad(gName,ip,port,Network.INIT.name());
                            InitServer server = new InitServer(quad);
                            inits.add(server);
                            System.out.println(server.start());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }

    public static class FastScanner {

        private StringTokenizer tokenizer;

        public FastScanner(InputStream is) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 1024);
                byte[] buf = new byte[1024];
                while (true) {
                    int read = is.read(buf);
                    if (read == -1)
                        break;
                    bos.write(buf, 0, read);
                }
                tokenizer = new StringTokenizer(new String(bos.toByteArray()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean hasNext() {
            return tokenizer.hasMoreTokens();
        }

        public int nextInt() {
            return Integer.parseInt(tokenizer.nextToken());
        }

        public long nextLong() {
            return Long.parseLong(tokenizer.nextToken());
        }

        public double nextDouble() {
            return Double.parseDouble(tokenizer.nextToken());
        }

        public String nextString() {
            return tokenizer.nextToken("\n");
        }

    }

    public static void getConfig(){
        InputStream is = null;
        try {
            is = new FileInputStream("init_config");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FastScanner in = new FastScanner(is);

        while(in.hasNext()){
            String s = in.nextString();
            servers.add(new StandartQuad(s));
        }

    }

}
