package MainServer;

import AbstractServer.AbstractServer;
import Network.Messages.ClientMessages.*;
import Network.Messages.DatabaseMessages.*;
import Network.Messages.InitMessages.MessageAskServerLoad;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.Network;
import Network.StandardQuad;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sergeybp on 27.10.16.
 */
public class MainServer extends AbstractServer {

    private Integer JOB_ID = 0;
    private static final Integer USERS_BOUND = 20;

    private StandardQuad db;
    private HashMap<Integer, StandardQuad> jobs = new HashMap<>();
    private ArrayList<String> users = new ArrayList<>();

    public MainServer(StandardQuad quad) {
        super(quad);
        db = new StandardQuad("DB", "0.0.0.0", 8989, Network.DATABASE.name());
    }

    private void askServerLoad(YarikMessage gotMessage) {
        StandardQuad backAddress = getStandardQuad(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add(checkUsersSize() ? "AVAILABLE" : "NO");

        YarikMessage message = new MessageAskServerLoad();
        send(message, content, backAddress);
    }

    private void register(YarikMessage gotMessage) {
        putNewAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("" + JOB_ID);
        content.add(gotMessage.getMessageContent().get(1).getValue());
        content.add(gotMessage.getMessageContent().get(2).getValue());

        YarikMessage message = new MessageCreateUser();
        send(message, content, db);
    }

    private void createUser(YarikMessage gotMessage) {
        StandardQuad backAddress = takeBackAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add(gotMessage.getMessageContent().get(2).getValue());
        content.add("  ");

        YarikMessage message = new MessageRegister();
        send(message, content, backAddress);
    }

    private void getQueueForUser(YarikMessage gotMessage) {
        StandardQuad backAddress = takeBackAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("  ");
        content.add("  ");
        content.add(gotMessage.getMessageContent().get(4).getValue());

        YarikMessage message = new MessageGet();
        send(message, content, backAddress);
    }

    private void get(YarikMessage gotMessage) {
        putNewAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("" + JOB_ID);
        content.add(gotMessage.getMessageContent().get(1).getValue());
        content.add(gotMessage.getMessageContent().get(2).getValue());
        content.add(gotMessage.getMessageContent().get(3).getValue());

        YarikMessage message = new MessageGetQueueForUser();
        send(message, content, db);
    }

    private void publish(YarikMessage gotMessage) {
        putNewAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("" + JOB_ID);
        content.add(gotMessage.getMessageContent().get(1).getValue());
        content.add(gotMessage.getMessageContent().get(2).getValue());
        content.add(gotMessage.getMessageContent().get(3).getValue());

        YarikMessage message = new MessageNewPost();
        send(message, content, db);
    }

    private void send(YarikMessage message, ArrayList<String> content, StandardQuad address) {
        try {
            message.setFieldsContent(content);
        } catch (Exception e) {
            throw new MainServerException("Content is not full");
        }
        final JSONObject encodedMessage = message.encode();
        sendJSON(address.ip, address.port, encodedMessage);
    }

    private void newPost(YarikMessage gotMessage) {
        StandardQuad backAddress = takeBackAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add(gotMessage.getMessageContent().get(2).getValue());
        content.add(gotMessage.getMessageContent().get(3).getValue());
        content.add("  ");

        YarikMessage message = new MessagePublish();
        send(message, content, backAddress);
    }

    private void getRate(YarikMessage gotMessage) {
        putNewAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("" + JOB_ID);
        content.add(gotMessage.getMessageContent().get(1).getValue());

        YarikMessage message = new MessageGetRateDB();
        send(message, content, db);
    }

    private void feedback(YarikMessage gotMessage) {
        putNewAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("" + JOB_ID);
        content.add(gotMessage.getMessageContent().get(2).getValue());

        YarikMessage message = new MessageFeedbackDB();
        send(message, content, db);
    }

    private void getRateDB(YarikMessage gotMessage) {
        StandardQuad backAddress = takeBackAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("Here you are ^^");
        content.add(gotMessage.getMessageContent().get(2).getValue());

        YarikMessage message = new MessageGetRate();
        send(message, content, backAddress);
    }

    private void feedbackDB(YarikMessage gotMessage) {
        StandardQuad backAddress = takeBackAddress(gotMessage);

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add("Feedback status");
        content.add(gotMessage.getMessageContent().get(2).getValue());

        YarikMessage message = new MessageFeedback();
        send(message, content, backAddress);
    }

    private StandardQuad takeBackAddress(YarikMessage gotMessage) {
        Integer tmpJob = Integer.parseInt(gotMessage.getMessageContent().get(1).getValue());
        StandardQuad backAddress = jobs.get(tmpJob);
        jobs.remove(tmpJob);
        return backAddress;
    }

    private StandardQuad getStandardQuad(YarikMessage gotMessage) {
        return new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
    }

    private StandardQuad putNewAddress(YarikMessage gotMessage) {
        StandardQuad standardQuad = getStandardQuad(gotMessage);
        JOB_ID++;
        jobs.put(JOB_ID, standardQuad);
        return standardQuad;
    }

    @Override
    public void handleReceive(JSONObject object) {
        YarikMessage gotMessage = new MessageUnknown();
        try {
            gotMessage = gotMessage.decode(object);
        } catch (Exception e) {
            throw new MainServerException("Json is not full" + e.getMessage());
        }

        switch (gotMessage.getMessageType()) {
            case ASKESERVERLOAD:
                askServerLoad(gotMessage);
                break;
            case REGISTER:
                register(gotMessage);
                break;
            case CREATE_USER:
                createUser(gotMessage);
                break;
            case GET_QUEUE_FOR_USER:
                getQueueForUser(gotMessage);
                break;
            case GETUSERINFO:
            case SETUSERINFO:
            case MANAGE:
                break;
            case GET:
                get(gotMessage);
                break;
            case PUBLISH:
                publish(gotMessage);
                break;
            case NEW_POST:
                newPost(gotMessage);
                break;
            case GET_RATE:
                getRate(gotMessage);
                break;
            case FEEDBACK:
                feedback(gotMessage);
                break;
            case GET_RATE_DB:
                getRateDB(gotMessage);
                break;
            case FEEDBACK_DB:
                feedbackDB(gotMessage);
                break;
        }
    }

    private boolean checkUsersSize() {
        return users.size() <= USERS_BOUND;
    }
}
