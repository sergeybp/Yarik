package Database;

import Network.Messages.DatabaseMessages.MessageCreateUser;
import Network.Messages.DatabaseMessages.MessageGetQueueForUser;
import Network.Messages.DatabaseMessages.MessageNewPost;
import Network.Messages.InitMessages.MessageAskServerLoad;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.StandartQuad;
import abstractttt.AbstractServer;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by sergeybp on 21.11.16.
 */
public class DatabaseServer extends AbstractServer {

    Integer ID = 0;
    Integer POSTID = 0;

    ArrayList<String> jobsId = new ArrayList<>();

    ArrayList<String> users = new ArrayList<>();

    //TODO all what you see - just a model. we HAVE to reconstruct this fuck.


    //All about users
    ArrayList<Integer> userIds = new ArrayList<>();
    //name$something
    HashMap<Integer, String> userInfos = new HashMap<>();
    HashMap<Integer, String> userTags = new HashMap<>();
    HashMap<Integer, String> userPosts = new HashMap<>();
    HashMap<Integer, String> userLikes = new HashMap<>();

    //All messages
    //id$text$rate$userid$tags(..$..$..)
    ArrayList<String> posts = new ArrayList<>();

    //All tags
    ArrayList<String> tags = new ArrayList<>();

    boolean registerUser(String info, String tags) {
        for (HashMap.Entry<Integer, String> tmp : userInfos.entrySet()) {
            String tt = tmp.getValue();
            String[] splits = tt.split("\\$");
            String[] splits1 = info.split("\\$");
            if (splits[0].equals(splits1[0])) {
                return false;
            }
        }
        userIds.add(ID);
        userInfos.put(ID, info);
        userTags.put(ID, tags);
        userPosts.put(ID, "");
        userLikes.put(ID, "");
        ID++;
        return true;
    }

    boolean updateInfoUser(Integer userId, String info, String tags) {
        if (!userIds.contains(userId)) {
            return false;
        }
        userInfos.put(userId, info);
        userTags.put(userId, tags);
        return true;
    }

    boolean getFeedback(Integer userId, String messagesIds, String likes) {
        if (!userIds.contains(userId)) {
            return false;
        }
        String[] messages = messagesIds.split("\\$");
        String[] like = likes.split("\\$");

        for (int i = 0; i < messages.length; i++) {

            //update userLikes on server
            if (like[i].equals("+")) {
                String tmp = userLikes.get(userId);
                String[] tmp1 = tmp.split("\\$");
                boolean flag = false;
                for (int j = 0; j < tmp1.length; j++) {
                    if (tmp1[j].equals(messages[i])) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    tmp += messages[i] + "$";
                    userLikes.put(userId, tmp);
                }
            }

            //update messageRate on server
            for (int j = 0; j < posts.size(); j++) {
                String tmpMessage = posts.get(j);
                String[] splits = tmpMessage.split("\\$");
                if (splits[0].equals(messages[i])) {
                    Integer rate = Integer.parseInt(splits[2]);
                    if (like[i].equals("+")) {
                        rate++;
                    } else {
                        rate--;
                    }
                    splits[2] = "" + rate;
                    String save = "";
                    for (int m = 0; m < splits.length; m++) {
                        save += splits[m] + "$";
                    }
                    posts.set(j, save);
                    break;
                }
            }
        }
        return true;
    }

    boolean getPost(Integer userId, String post, String tags) {
        String toSave = "" + POSTID + "$";
        POSTID++;
        toSave += post + "$" + "0" + "$" + userId + "$" + tags;
        posts.add(toSave);
        return true;
    }

    String getMessageQueue(Integer userId, Integer lastRead) {
        ArrayList<Pair<Integer,Integer>> preQueue = new ArrayList<>();
        for(int i = 0; i < posts.size();i++ ){
            String now = posts.get(i);
            String[] splits = now.split("\\$");
            if(Integer.parseInt(splits[0]) < lastRead){
                continue;
            }
            String uTags = userTags.get(userId);
            String[] tags = uTags.split("\\$");
            int bal = 0;
            for(int k = 0 ; k < tags.length;k++) {
                for (int j = 4; j < splits.length; j++) {
                    if(splits[j].equals(tags[k])){
                        bal++;
                    }
                }
            }
            if(bal >= 2){
                preQueue.add(new Pair<Integer,Integer>(Integer.parseInt(splits[0]),Integer.parseInt(splits[2])));
            }
        }
        Collections.sort(preQueue,myC);
        int bound = Math.min(preQueue.size(),20);
        String toSend = "";
        for(int i = 0 ; i < bound; i++){
            int tmp = preQueue.get(i).getKey();
            for(int j = 0 ; j < posts.size(); j++){
                String now = posts.get(j);
                String[] splits = now.split("\\$");
                if(Integer.parseInt(splits[0]) == tmp){
                    toSend+=now+"^";
                    break;
                }
            }
        }
        return toSend;
    }


    public DatabaseServer(StandartQuad quad) {
        super(quad);
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
            case CREATEUSER:
                StandartQuad backAddress = new StandartQuad(gotMessage.getMessageContent().get(0).getValue());
                String info = gotMessage.getMessageContent().get(2).getValue();
                String tags = gotMessage.getMessageContent().get(3).getValue();
                YarikMessage message = new MessageCreateUser();
                ArrayList<String> content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add(gotMessage.getMessageContent().get(1).getValue());
                if (registerUser(info, tags)) {
                    content.add("OK");
                    content.add("OK");
                } else {
                    content.add("NO");
                    content.add("NO");
                }
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject object1 = message.encode();
                try {
                    sendJSON(backAddress.ip, backAddress.port, object1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showStatus();
                break;
            case NEWPOST:
                backAddress = new StandartQuad(gotMessage.getMessageContent().get(0).getValue());
                String post = gotMessage.getMessageContent().get(3).getValue();
                tags = gotMessage.getMessageContent().get(4).getValue();
                message = new MessageNewPost();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add(gotMessage.getMessageContent().get(1).getValue());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                Integer id = getId(gotMessage.getMessageContent().get(2).getValue());
                if (id == -1) {
                    content.add("NO");
                    content.add("NO");
                } else if (getPost(id, post, tags)) {
                    content.add("OK");
                    content.add("OK");
                } else {
                    content.add("NO");
                    content.add("NO");
                }
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                try {
                    sendJSON(backAddress.ip, backAddress.port, object1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showStatus();
                break;
            case GETQUEUEFORUSER:
                backAddress = new StandartQuad(gotMessage.getMessageContent().get(0).getValue());
                message = new MessageGetQueueForUser();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add(gotMessage.getMessageContent().get(1).getValue());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                content.add(gotMessage.getMessageContent().get(3).getValue());
                id = getId(gotMessage.getMessageContent().get(2).getValue());
                if (id == -1) {
                    content.add("NO");
                } else{
                    content.add(getMessageQueue(id,Integer.parseInt(gotMessage.getMessageContent().get(3).getValue())));
                }
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                try {
                    sendJSON(backAddress.ip, backAddress.port, object1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showStatus();
                break;
        }
    }


    public void showStatus() {
        String toShow = "";
        for (int i = 0; i < userIds.size(); i++) {
            toShow += "" + userIds.get(i) + " -- " + userInfos.get(userIds.get(i)) + " -- " + userTags.get(userIds.get(i)) + "\n";
        }
        for (int i = 0; i < posts.size(); i++) {
            toShow += ""+posts.get(i)+"\n";
        }
        System.err.print(toShow);
    }

    Integer getId(String name) {
        for (HashMap.Entry<Integer, String> tmp : userInfos.entrySet()) {
            String tt = tmp.getValue();
            String[] splits = tt.split("\\$");
            if (splits[0].equals(name)) {
                return tmp.getKey();
            }
        }
        return -1;
    }

    Comparator<Pair<Integer,Integer>> myC = new Comparator<Pair<Integer, Integer>>() {
        @Override
        public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
            return Integer.compare(o1.getValue(), o2.getValue());
        }
    };
}
