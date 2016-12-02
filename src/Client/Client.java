package Client;

import Network.Messages.ClientMessages.MessageGet;
import Network.Messages.ClientMessages.MessagePublish;
import Network.Messages.ClientMessages.MessageRegister;
import Network.Messages.InitMessages.MessageAskForServer;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.Network;
import Network.StandardQuad;
import abstractttt.AbstractServer;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sergeybp on 27.10.16.
 */

//This is an example implementation of Client for Yarik.
public class Client extends AbstractServer {

    ArrayList<String> myPosts = new ArrayList<>();
    ArrayList<Pair<String, String>> myLikes = new ArrayList<>();

    String myName = "";

    static StandardQuad myQuad = new StandardQuad("CLIE1", "0.0.0.0", 4578, Network.CLIENT.name());
    StandardQuad mainServer;

    public Client() {
        super(myQuad);
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
            case REGISTER:
                if (gotMessage.getMessageContent().get(1).getValue().equals("NO")) {
                    myName = "";
                }
                System.err.println(gotMessage.getMessageContent().get(1).getValue());
                break;
            case ASKFORSERVER:
                mainServer = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                System.err.println("GOT MAIN ADDR: " + mainServer.toString());
                break;
            case PUBLISH:
                System.err.println(gotMessage.getMessageContent().get(2).getValue());
                break;
            case GET:
                String toShow = "";
                String tmp = gotMessage.getMessageContent().get(3).getValue();
                String[] splits = tmp.split("\\^");
                for (String split : splits) {
                    String[] ss = split.split("\\$");
                    toShow += ss[0] + " -- " + ss[1] + "\n";
                }
                System.err.print(toShow);
                break;
        }
    }


    public static void main(String[] args) {
        new Client().run();
    }

    void run() {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
                        switch (slices[0]) {
                            case "init":
                                StandardQuad initQuad = getInitFromConfig();
                                sendAskForServer(initQuad);
                                System.err.println("SENT ASK TO INIT");
                                break;
                            case "register": {
                                if (!myName.equals("")) {
                                    System.err.println("ALREADY REGISTERED!");
                                    continue;
                                }
                                String name = slices[1];
                                String something = "";
                                for (int i = 2; i < slices.length; i++) {
                                    something += slices[i];
                                }
                                YarikMessage message = new MessageRegister();
                                ArrayList<String> content = new ArrayList<String>();
                                content.add(myQuad.toString());
                                myName = name;
                                content.add(name + "$" + something);
                                content.add("tag1$tag2$tag3$tag4");
                                message.setFieldsContent(content);
                                JSONObject object = message.encode();
                                sendJSON(mainServer.ip, mainServer.port, object);
                                break;
                            }
                            case "post": {
                                String post = "";
                                String tags = "";
                                int a = 0;
                                for (int i = 1; i < slices.length; i++) {
                                    if (slices[i].equals("|")) {
                                        a = 1;
                                        continue;
                                    }
                                    if (a == 1) {
                                        tags += slices[i] + "$";
                                    }
                                    if (a == 0) {
                                        post += slices[i] + " ";
                                    }
                                }
                                tags = tags.substring(0, tags.length() - 1);
                                YarikMessage message = new MessagePublish();
                                ArrayList<String> content = new ArrayList<String>();
                                content.add(myQuad.toString());
                                content.add(myName);
                                content.add(post);
                                content.add(tags);
                                message.setFieldsContent(content);
                                JSONObject object = message.encode();
                                sendJSON(mainServer.ip, mainServer.port, object);
                                break;
                            }
                            case "get": {
                                String last = slices[1];
                                YarikMessage message = new MessageGet();
                                ArrayList<String> content = new ArrayList<String>();
                                content.add(myQuad.toString());
                                content.add(myName);
                                content.add(last);
                                content.add("  ");
                                message.setFieldsContent(content);
                                JSONObject object = message.encode();
                                sendJSON(mainServer.ip, mainServer.port, object);
                                break;
                            }
                            case "spam":
                                spam();
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


    void spam() {
        for (int i = 0; i < 10; i++) {
            Random r = new Random();
            YarikMessage message = new MessagePublish();
            ArrayList<String> content = new ArrayList<String>();
            content.add(myQuad.toString());
            content.add(myName);
            content.add("" + r.nextInt(7000));
            content.add("tag1$tag2$tag3");
            try {
                message.setFieldsContent(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject object = message.encode();

                sendJSON(mainServer.ip, mainServer.port, object);
        }
        for (int i = 0; i < 5; i++) {
            Random r = new Random();
            YarikMessage message = new MessagePublish();
            ArrayList<String> content = new ArrayList<String>();
            content.add(myQuad.toString());
            content.add(myName);
            content.add("" + r.nextInt(7000));
            content.add("tag1$tag2");
            try {
                message.setFieldsContent(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject object = message.encode();
                sendJSON(mainServer.ip, mainServer.port, object);
        }
    }

    public StandardQuad getInitFromConfig() {
        // TODO Now we actually don't have configs :(
        return new StandardQuad("TESTINIT", "0.0.0.0", 8765, Network.INIT.name());
    }

    public void sendAskForServer(StandardQuad init) {
        YarikMessage message = new MessageAskForServer();
        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("GIVE");
        try {
            message.setFieldsContent(content);
        } catch (Exception e) {
            //here we can get exception if content not full
            e.printStackTrace();
        }
        JSONObject object = message.encode();
            sendJSON(init.ip, init.port, object);

    }

}
