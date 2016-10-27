package Network.Messages;

import Network.Messages.ClientMessages.MessageGet;
import Network.Messages.ClientMessages.MessageManage;
import Network.Messages.ClientMessages.MessagePublish;
import Network.Messages.InitMessages.MessageAskForServer;
import Network.Messages.InitMessages.MessageReturnServerAddress;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by sergeybp on 22.09.16.
 */
public abstract class YarikMessage {

    String TYPE = "TYPE";

    ArrayList<Pair<String,String>> messageContent = new ArrayList<>();

    public abstract YarikMessageType getMessageType();

    public abstract YarikMessageField[] getMessageFields();

    public void setFieldsContent(ArrayList<String> content) throws Exception {
        ArrayList<YarikMessageField> fields = new ArrayList<>(Arrays.asList(getMessageFields()));
        if(!(fields.size() == content.size())){
            //TODO
            throw new Exception("Content not FULL");
        }
        messageContent = new ArrayList<>();
        for(int i = 0 ; i < fields.size(); i++){
            messageContent.add(new Pair<>(fields.get(i).name(),content.get(i)));
        }
    }

    public ArrayList<Pair<String,String>> getMessageContent(){
        return messageContent;
    }

    public JSONObject encode(){
        JSONObject result = new JSONObject();
        result.put(TYPE,getMessageType().name());
        for(Pair<String,String> now : messageContent){
            result.put(now.getKey(),now.getValue());
        }
        return result;
    }

    public YarikMessage decode(JSONObject object) throws Exception {

        String type = object.get(TYPE).toString();
        //TODO
        if(type.equals(YarikMessageType.PUBLISH.name())){
            YarikMessage message = new MessagePublish();
            YarikMessageField[] fields = message.getMessageFields();
            ArrayList<String> content = new ArrayList<>();
            for(YarikMessageField field : fields){
                content.add(object.get(field.name()).toString());
            }
            message.setFieldsContent(content);
            return message;
        }
        if(type.equals(YarikMessageType.MANAGE.name())){
            YarikMessage message = new MessageManage();
            YarikMessageField[] fields = message.getMessageFields();
            ArrayList<String> content = new ArrayList<>();
            for(YarikMessageField field : fields){
                content.add(object.get(field.name()).toString());
            }
            message.setFieldsContent(content);
            return message;
        }
        if(type.equals(YarikMessageType.GET.name())){
            YarikMessage message = new MessageGet();
            YarikMessageField[] fields = message.getMessageFields();
            ArrayList<String> content = new ArrayList<>();
            for(YarikMessageField field : fields){
                content.add(object.get(field.name()).toString());
            }
            message.setFieldsContent(content);
            return message;
        }
        if(type.equals(YarikMessageType.ASKFORSERVER.name())){
            YarikMessage message = new MessageAskForServer();
            YarikMessageField[] fields = message.getMessageFields();
            ArrayList<String> content = new ArrayList<>();
            for(YarikMessageField field : fields){
                content.add(object.get(field.name()).toString());
            }
            message.setFieldsContent(content);
            return message;
        }
        if(type.equals(YarikMessageType.RETURNSERVERADDRESS.name())){
            YarikMessage message = new MessageReturnServerAddress();
            YarikMessageField[] fields = message.getMessageFields();
            ArrayList<String> content = new ArrayList<>();
            for(YarikMessageField field : fields){
                content.add(object.get(field.name()).toString());
            }
            message.setFieldsContent(content);
            return message;
        }

        return null;
    }

}