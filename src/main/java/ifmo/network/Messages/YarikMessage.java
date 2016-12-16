package ifmo.network.Messages;

import ifmo.network.Messages.ClientMessages.*;
import ifmo.network.Messages.DatabaseMessages.*;
import ifmo.network.Messages.InitMessages.MessageAskForServer;
import ifmo.network.Messages.InitMessages.MessageAskServerLoad;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by sergeybp on 22.09.16.
 */
public abstract class YarikMessage {

    public String TYPE = "TYPE";
    public ArrayList<Pair<String, String>> messageContent = new ArrayList<>();

    public abstract YarikMessageType getMessageType();

    public abstract YarikMessageField[] getMessageFields();

    public void setFieldsContent(ArrayList<String> content) {
        ArrayList<YarikMessageField> fields = new ArrayList<>(Arrays.asList(getMessageFields()));
        if (!(fields.size() == content.size())) {
            throw new YarikMessageException("Content is not full");
        }
        messageContent = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            messageContent.add(new Pair<>(fields.get(i).name(), content.get(i)));
        }
    }

    public ArrayList<Pair<String, String>> getMessageContent() {
        return messageContent;
    }

    public JSONObject encode() {
        JSONObject result = new JSONObject();
        result.put(TYPE, getMessageType().name());
        for (Pair<String, String> now : messageContent) {
            result.put(now.getKey(), now.getValue());
        }
        return result;
    }

    private YarikMessage handleObject(JSONObject object) {
        String type = object.get(TYPE).toString();
        switch (YarikMessageType.valueOf(type)) {
            case PUBLISH: return new MessagePublish();
            case MANAGE: return new MessageManage();
            case GET: return new MessageGet();
            case ASKFORSERVER: return new MessageAskForServer();
            case ASKESERVERLOAD: return new MessageAskServerLoad();
            case GET_QUEUE_FOR_USER: return new MessageGetQueueForUser();
            case GETUSERINFO: return new MessageGetQueueForUser();
            case SETUSERINFO: return new MessageSetUserInfo();
            case CREATE_USER: return new MessageCreateUser();
            case REGISTER: return new MessageRegister();
            case NEW_POST: return new MessageNewPost();
            case FEEDBACK: return new MessageFeedback();
            case GET_RATE: return new MessageGetRate();
            case FEEDBACK_DB: return new MessageFeedbackDB();
            case GET_RATE_DB: return new MessageGetRateDB();
        }
        return null;
    }

    public YarikMessage decode(JSONObject object) throws Exception {
        YarikMessage message = handleObject(object);

        if (message == null)  {
            throw new YarikMessageException("Unknown message");
        }

        YarikMessageField[] fields = message.getMessageFields();
        ArrayList<String> content = new ArrayList<>();

        for (YarikMessageField field : fields) {
            content.add(object.get(field.name()).toString());
        }
        message.setFieldsContent(content);
        return message;
    }

}