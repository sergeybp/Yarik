package ifmo.client;


import ifmo.network.Messages.ClientMessages.*;
import ifmo.network.Messages.InitMessages.MessageAskForServer;
import ifmo.network.Messages.MessageUnknown;
import ifmo.network.Messages.YarikMessage;
import ifmo.network.Network;
import ifmo.network.StandardQuad;
import ifmo.server.AbstractServer;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Created by sergeybp on 27.10.16.
 */

//This is an example implementation of Client for Yarik.
public class Client extends AbstractServer {

    int isNew = 0;
    boolean beforePost = false;
    int nowRead = 0;
    String[] tmpSlices;
    String filepr = "client.properties1";
    ArrayList<String> myPosts = new ArrayList<>();
    ArrayList<Pair<String, String>> myLikes = new ArrayList<>();
    ArrayList<Pair<String, String>> nowPosts = new ArrayList<>();

    ArrayList<String> possibleTags = new ArrayList<>();

    String myName = "";

    static StandardQuad myQuad = new StandardQuad("CLIE1", "0.0.0.0", 4558, Network.CLIENT.name());
    StandardQuad mainServer;

    Properties getProperties() throws IOException {
        FileInputStream inputStream = new FileInputStream(getClass().getClassLoader().getResource(filepr).getFile());
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    void saveProerties(String key, String val) throws IOException {
        FileInputStream inputStream = new FileInputStream(filepr);
        Properties properties = new Properties();
        properties.load(inputStream);
        properties.setProperty(key, val);
        File f = new File(filepr);
        OutputStream out = new FileOutputStream(f);
        properties.save(out, "ok");
        out.close();
    }


    void readNext() {
        if (nowRead == nowPosts.size()) {
            nowRead = 0;
            startRead();
            return;
        }
        if (myLikes.size() > 2) {
            pushFeedback();
        }
        System.out.println(nowPosts.get(nowRead).getValue() + "\n" +
                "1 -- like, 2 -- dislike, ex -- stop reading");
        nowRead++;
    }

    void plusRate() {
        myLikes.add(new Pair<>(nowPosts.get(nowRead - 1).getKey(), "+"));
        readNext();
    }

    void minusRate() {
        myLikes.add(new Pair<>(nowPosts.get(nowRead - 1).getKey(), "-"));
        readNext();
    }

    void exitRead() {
        try {
            Properties p = getProperties();
            int k = Integer.parseInt(p.getProperty("last"));
            k -= (nowPosts.size() - nowRead);
            saveProerties("last", "" + k);
            nowRead = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void post() {
        String post = "";
        String tags = "";
        int a = 0;
        for (int i = 1; i < tmpSlices.length; i++) {
            if (tmpSlices[i].equals("|")) {
                a = 1;
                continue;
            }
            if (a == 1) {
                tags += possibleTags.get(Integer.parseInt(tmpSlices[i])) + "$";
            }
            if (a == 0) {
                post += tmpSlices[i] + " ";
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
    }

    void startRead() {
        Properties p = new Properties();
        Boolean f = true;
        try {
            p = getProperties();
        } catch (IOException e) {
            f = false;
            e.printStackTrace();
        }
        if (p.size() < 2) {
            f = false;
        }
        int last = 0;
        if (f) {
            last = Integer.parseInt(p.getProperty("last"));
        }
        YarikMessage message = new MessageGet();
        ArrayList<String> content = new ArrayList<String>();
        content.add(myQuad.toString());
        content.add(myName);
        content.add("" + last);
        content.add("  ");
        message.setFieldsContent(content);
        JSONObject object = message.encode();
        sendJSON(mainServer.ip, mainServer.port, object);
        System.out.println("Loading posts...");
    }

    public Client() {
        super(myQuad);
        possibleTags.add("cats");
        possibleTags.add("dogs");
        possibleTags.add("politics");
        possibleTags.add("travel");
        possibleTags.add("food");
        Properties properties = null;
        try {
            properties = getProperties();
        } catch (IOException e) {
            isNew = 1;
            e.printStackTrace();
            return;
        }
        if (properties.size() == 0) {
            isNew = 1;
            return;
        }
        myName = properties.getProperty("name");

    }


    void register(String name, String otherInfo, String tags) {
        YarikMessage message = new MessageRegister();
        ArrayList<String> content = new ArrayList<String>();
        content.add(myQuad.toString());
        myName = name;
        content.add(name + "$" + otherInfo);
        content.add(tags.substring(0, tags.length() - 1));
        message.setFieldsContent(content);
        JSONObject object = message.encode();
        sendJSON(mainServer.ip, mainServer.port, object);
    }

    void pushFeedback() {
        YarikMessage message = new MessageFeedback();
        ArrayList<String> content = new ArrayList<String>();
        content.add(myQuad.toString());
        String s = "";
        for (int i = 0; i < myLikes.size() - 1; i++) {
            s += myLikes.get(i).getKey() + "$";
        }
        s += myLikes.get(myLikes.size() - 1).getKey() + "|";
        for (int i = 0; i < myLikes.size() - 1; i++) {
            s += myLikes.get(i).getValue() + "$";
        }
        s += myLikes.get(myLikes.size() - 1).getValue();
        content.add("FeedBack");
        content.add(s);
        message.setFieldsContent(content);
        myLikes = new ArrayList<>();
        JSONObject object = message.encode();
        sendJSON(mainServer.ip, mainServer.port, object);
    }

    void getRate() {
        YarikMessage message = new MessageGetRate();
        ArrayList<String> content = new ArrayList<String>();
        content.add(myQuad.toString());
        content.add(myName);
        content.add("Give me my rate! :)");
        message.setFieldsContent(content);
        JSONObject object = message.encode();
        sendJSON(mainServer.ip, mainServer.port, object);
    }

    void init() {
        StandardQuad initQuad = getInitFromConfig();
        sendAskForServer(initQuad);
        //System.err.println("SENT ASK TO INIT");
    }

    void tags() {
        System.out.println("Tags to use:");
        for (int i = 0; i < possibleTags.size(); i++) {
            System.out.println("" + i + " -- " + possibleTags.get(i));
        }
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
                    System.out.println("You're now logged in as " + myName);
                    break;
                }
                //System.err.println(gotMessage.getMessageContent().get(1).getValue());
                System.out.println("Account created! Hi, " + myName);
                try {
                    saveProerties("name", myName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ASKFORSERVER:
                mainServer = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                //System.err.println("GOT MAIN ADDR: " + mainServer.toString());
                System.out.println("Connected to Yarik!\n" +
                        "Use help command to start");
                if (isNew == 0) {
                    System.out.println("Welcome back, " + myName);
                }
                break;
            case PUBLISH:
                //System.err.println(gotMessage.getMessageContent().get(2).getValue());
                System.out.println("Message published!");
                break;
            case GET:
                String toShow = "";
                nowPosts = new ArrayList<>();
                String tmp = gotMessage.getMessageContent().get(3).getValue();
                if (tmp.equals("")) {
                    System.out.println(":( No more posts to show...");
                    return;
                }
                String[] splits = tmp.split("\\^");
                for (String split : splits) {
                    String[] ss = split.split("\\$");
                    nowPosts.add(new Pair<>(ss[0], ss[1]));
                    toShow += ss[0] + " -- " + ss[1] + "\n";
                }
                Properties p = new Properties();
                try {
                    p = getProperties();
                } catch (Exception e) {

                    e.printStackTrace();
                }

                if (p.size() >= 2) {
                    int k = Integer.parseInt(p.getProperty("last"));
                    k += nowPosts.size();
                    try {
                        saveProerties("last", "" + k);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        saveProerties("last", "" + (nowPosts.size() + 1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //System.err.print(toShow);
                readNext();
                break;
            case GET_RATE:
                if (beforePost) {
                    String rate = gotMessage.getMessageContent().get(2).getValue();
                    Double now = Double.valueOf(rate);
                    beforePost = false;
                    if (now >= 0d) {
                        System.out.println("All ok! Posting...");
                        post();
                    } else {
                        System.out.println("You don't have enough rating...");
                    }
                    return;
                }
                String rate = gotMessage.getMessageContent().get(2).getValue();
                System.out.println("Your current rate: " + rate);
                return;
            case FEEDBACK:
                String fe = gotMessage.getMessageContent().get(2).getValue();
                System.out.println("FeedbackStatus: " + fe);
                return;
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
                System.out.println("Welcome to Yarik! \nPlease wait...");
                init();
                while (true) {
                    try {
                        String line = reader.readLine();
                        if (line == null || line.equals("exit")) {
                            System.exit(0);
                            return;
                        }
                        String[] slices = line.trim().split(" +");
                        switch (slices[0]) {
                            case "help":
                                System.out.println("Possible commands\n" +
                                        "register <name> | <personal info> | <tags indexes> -- will register you in system or login if account exists.\n" +
                                        "post <message> | <tags indexes> -- will post new message\n" +
                                        "read -- start reading mode\n" +
                                        "info -- get info about account\n" +
                                        "tags -- shows possible tags\n\n");
                                tags();
                                break;
                            case "tags":
                                tags();
                                break;
                            case "register": {

                                String name = slices[1];
                                String something = "";
                                int mode = 0;
                                String tags = "";
                                for (int i = 3; i < slices.length; i++) {
                                    if (slices[i].equals("|")) {
                                        mode = 1;
                                        continue;
                                    }
                                    if (mode == 0) {
                                        something += slices[i] + " ";
                                    } else {
                                        tags += possibleTags.get(Integer.parseInt(slices[i])) + "$";
                                    }
                                }
                                register(name, something, tags);

                                break;
                            }
                            case "post": {
                                System.out.println("Wait.. Checking your rating.");
                                beforePost = true;
                                tmpSlices = slices;
                                getRate();
                                break;
                            }
                            case "read": {
                                startRead();
                                break;
                            }
                            case "spam":
                                spam();
                                break;
                            case "ex":
                                exitRead();
                                break;
                            case "1":
                                plusRate();
                                break;
                            case "2":
                                minusRate();
                                break;
                            case "info":
                                getRate();
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
        for (int i = 0; i < 2; i++) {
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
