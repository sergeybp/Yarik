package MainServer;

import Network.Messages.ClientMessages.MessageGet;
import Network.Messages.ClientMessages.MessagePublish;
import Network.Messages.ClientMessages.MessageRegister;
import Network.Messages.DatabaseMessages.MessageCreateUser;
import Network.Messages.DatabaseMessages.MessageGetQueueForUser;
import Network.Messages.DatabaseMessages.MessageNewPost;
import Network.Messages.InitMessages.MessageAskForServer;
import Network.Messages.InitMessages.MessageAskServerLoad;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.Network;
import Network.StandartQuad;
import abstractttt.AbstractServer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by sergeybp on 27.10.16.
 */
public class MainServer extends AbstractServer {

    ArrayList<String> jobsId = new ArrayList<>();
    StandartQuad db;
    HashMap<Integer, StandartQuad> jobs= new HashMap<>();

    Integer JOBID = 0;

    ArrayList<String> users = new ArrayList<>();
    Integer USERSBOUND = 20;

    public MainServer(StandartQuad quad) {
        super(quad);
        db = new StandartQuad("DB","0.0.0.0",8989,Network.DATABASE.name());
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
            case ASKESERVERLOAD:
                StandartQuad backAddress = new StandartQuad(gotMessage.getMessageContent().get(0).getValue());
                YarikMessage message = new MessageAskServerLoad();
                ArrayList<String> content = new ArrayList<>();
                content.add(myQuad.toString());
                if(checkAvaibility()) {
                    content.add("AVAILABLE");
                } else {
                    content.add("NO");
                }
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    //here we can get exception if content not full
                    e.printStackTrace();
                }
                // getting JSON from message to send
                JSONObject object1 = message.encode();
                try {
                    sendJSON(backAddress.ip, backAddress.port, object1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REGISTER:
                backAddress = new StandartQuad(gotMessage.getMessageContent().get(0).getValue());
                JOBID++;
                jobs.put(JOBID, backAddress);
                message = new MessageCreateUser();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add(""+JOBID);
                content.add(gotMessage.getMessageContent().get(1).getValue());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                try {
                    sendJSON(db.ip, db.port, object1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case CREATEUSER:
                Integer tmpJob = Integer.parseInt(gotMessage.getMessageContent().get(1).getValue());
                backAddress = jobs.get(tmpJob);
                jobs.remove(tmpJob);
                message = new MessageRegister();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                content.add("  ");
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
                break;
            case GETQUEUEFORUSER:
                tmpJob = Integer.parseInt(gotMessage.getMessageContent().get(1).getValue());
                backAddress = jobs.get(tmpJob);
                jobs.remove(tmpJob);
                message = new MessageGet();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("  ");
                content.add("  ");
                content.add(gotMessage.getMessageContent().get(4).getValue());
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
                break;
            case GETUSERINFO:

                break;
            case SETUSERINFO:

                break;

            case MANAGE:

                break;

            case GET:
                backAddress = new StandartQuad(gotMessage.getMessageContent().get(0).getValue());
                JOBID++;
                jobs.put(JOBID, backAddress);
                message = new MessageGetQueueForUser();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add(""+JOBID);
                content.add(gotMessage.getMessageContent().get(1).getValue());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                content.add(gotMessage.getMessageContent().get(3).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                try {
                    sendJSON(db.ip, db.port, object1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case PUBLISH:
                backAddress = new StandartQuad(gotMessage.getMessageContent().get(0).getValue());
                JOBID++;
                jobs.put(JOBID, backAddress);
                message = new MessageNewPost();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add(""+JOBID);
                content.add(gotMessage.getMessageContent().get(1).getValue());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                content.add(gotMessage.getMessageContent().get(3).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                try {
                    sendJSON(db.ip, db.port, object1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case NEWPOST:
                tmpJob = Integer.parseInt(gotMessage.getMessageContent().get(1).getValue());
                backAddress = jobs.get(tmpJob);
                jobs.remove(tmpJob);
                message = new MessagePublish();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                content.add(gotMessage.getMessageContent().get(3).getValue());
                content.add("  ");
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
                break;
        }

    }


    void printStatus(){

    }

    public boolean checkAvaibility() {
        if(users.size() > USERSBOUND){
            return false;
        }
        return true;
    }

    public String createJobId(){
        String id = "";
        Random r  = new Random();
        for(int i = 0 ; i < 8 ; i ++){
            id+=""+r.nextInt(10);
        }
        return id;
    }
}
