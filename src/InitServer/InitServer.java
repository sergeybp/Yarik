package InitServer;

import Network.Messages.InitMessages.MessageAskForServer;
import Network.Messages.InitMessages.MessageAskServerLoad;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.StandardQuad;
import abstractttt.AbstractServer;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by sergeybp on 27.10.16.
 */
public class InitServer extends AbstractServer {


    ArrayList<StandardQuad> users = new ArrayList<>();
    ArrayList<StandardQuad> servers = new ArrayList<>();

    public InitServer(StandardQuad quad) {
        super(quad);
        getConfigs();
    }

    @Override
    public void handleReceive(JSONObject object) {
        YarikMessage gotMessage = new MessageUnknown();
        try {
            gotMessage = gotMessage.decode(object);
        } catch (Exception e) {
            // if JSON is not full
            e.printStackTrace();
        }

        switch (gotMessage.getMessageType()) {
            case ASKFORSERVER:
                StandardQuad user = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                users.add(user);

                YarikMessage message = new MessageAskServerLoad();
                ArrayList<String> content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("PLEASE GIVE");
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    //here we can get exception if content not full
                    e.printStackTrace();
                }

                // getting JSON from message to send
                JSONObject object1 = message.encode();


                for (StandardQuad server : servers) {
                        sendJSON(server.ip, server.port, object1);

                }

            case ASKESERVERLOAD:
                StandardQuad serverToRet = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                String available = gotMessage.getMessageContent().get(1).getValue();
                if (available.equals("AVAILABLE")) {
                    message = new MessageAskForServer();
                    content = new ArrayList<>();
                    content.add(serverToRet.toString());
                    content.add("Have a nice day");
                    try {
                        message.setFieldsContent(content);
                    } catch (Exception e) {
                        //here we can get exception if content not full
                        e.printStackTrace();
                    }

                    // getting JSON from message to send
                    object1 = message.encode();

                    if(users.size()>0) {
                        StandardQuad userNow = users.get(0);
                        users.remove(0);
                            sendJSON(userNow.ip, userNow.port, object1);
                    }

                } else {
                    return;
                }
                break;
        }
    }

    public void getConfigs() {

        InputStream is = null;
        try {
            is = new FileInputStream("init_config");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FastScanner in = new FastScanner(is);

        while(in.hasNext()){
            String s = in.nextString();
            servers.add(new StandardQuad(s));
        }

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


}
