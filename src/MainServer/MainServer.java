package MainServer;

import Network.Messages.ClientMessages.*;
import Network.Messages.DatabaseMessages.*;
import Network.Messages.InitMessages.MessageAskServerLoad;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.Network;
import Network.StandardQuad;
import abstractttt.AbstractServer;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by sergeybp on 27.10.16.
 */
public class MainServer extends AbstractServer {

    ArrayList<String> jobsId = new ArrayList<>();
    StandardQuad db;
    HashMap<Integer, StandardQuad> jobs = new HashMap<>();

    Integer JOBID = 0;

    ArrayList<String> users = new ArrayList<>();
    Integer USERSBOUND = 20;

    public MainServer(StandardQuad quad) {
        super(quad);
        db = new StandardQuad("DB", "0.0.0.0", 8989, Network.DATABASE.name());
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
                StandardQuad backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                YarikMessage message = new MessageAskServerLoad();
                ArrayList<String> content = new ArrayList<>();
                content.add(myQuad.toString());
                if (checkAvaibility()) {
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

                sendJSON(backAddress.ip, backAddress.port, object1);
                break;
            case REGISTER:
                backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                JOBID++;
                jobs.put(JOBID, backAddress);
                message = new MessageCreateUser();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("" + JOBID);
                content.add(gotMessage.getMessageContent().get(1).getValue());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                sendJSON(db.ip, db.port, object1);

                break;
            case CREATE_USER:
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

                sendJSON(backAddress.ip, backAddress.port, object1);
                break;
            case GET_QUEUE_FOR_USER:
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
                sendJSON(backAddress.ip, backAddress.port, object1);

                break;
            case GETUSERINFO:

                break;
            case SETUSERINFO:

                break;

            case MANAGE:

                break;

            case GET:
                backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                JOBID++;
                jobs.put(JOBID, backAddress);
                message = new MessageGetQueueForUser();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("" + JOBID);
                content.add(gotMessage.getMessageContent().get(1).getValue());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                content.add(gotMessage.getMessageContent().get(3).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();

                sendJSON(db.ip, db.port, object1);

                break;

            case PUBLISH:
                backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                JOBID++;
                jobs.put(JOBID, backAddress);
                message = new MessageNewPost();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("" + JOBID);
                content.add(gotMessage.getMessageContent().get(1).getValue());
                content.add(gotMessage.getMessageContent().get(2).getValue());
                content.add(gotMessage.getMessageContent().get(3).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                sendJSON(db.ip, db.port, object1);
                break;
            case NEW_POST:
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

                sendJSON(backAddress.ip, backAddress.port, object1);
                break;
            case GET_RATE:
                backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                JOBID++;
                jobs.put(JOBID, backAddress);
                message = new MessageGetRateDB();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("" + JOBID);
                content.add(gotMessage.getMessageContent().get(1).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                sendJSON(db.ip, db.port, object1);
                break;
            case FEEDBACK:
                backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
                JOBID++;
                jobs.put(JOBID, backAddress);
                message = new MessageFeedbackDB();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("" + JOBID);
                content.add(gotMessage.getMessageContent().get(2).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();
                sendJSON(db.ip, db.port, object1);
                break;
            case GET_RATEDB:
                tmpJob = Integer.parseInt(gotMessage.getMessageContent().get(1).getValue());
                backAddress = jobs.get(tmpJob);
                jobs.remove(tmpJob);
                message = new MessageGetRate();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("Here you are ^^");
                content.add(gotMessage.getMessageContent().get(2).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();

                sendJSON(backAddress.ip, backAddress.port, object1);
                break;
            case FEEDBACKDB:
                tmpJob = Integer.parseInt(gotMessage.getMessageContent().get(1).getValue());
                backAddress = jobs.get(tmpJob);
                jobs.remove(tmpJob);
                message = new MessageFeedback();
                content = new ArrayList<>();
                content.add(myQuad.toString());
                content.add("Feedback status");
                content.add(gotMessage.getMessageContent().get(2).getValue());
                try {
                    message.setFieldsContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                object1 = message.encode();

                sendJSON(backAddress.ip, backAddress.port, object1);
                break;
        }

    }


    void printStatus() {

    }

    public boolean checkAvaibility() {
        if (users.size() > USERSBOUND) {
            return false;
        }
        return true;
    }

    public String createJobId() {
        String id = "";
        Random r = new Random();
        for (int i = 0; i < 8; i++) {
            id += "" + r.nextInt(10);
        }
        return id;
    }
}
