package Database;

import Database.columns.Message;
import Database.columns.Tag;
import Database.columns.User;
import Database.dao.DAOFactory;
import Database.dao.ReflectionJdbcDao;
import Database.dao.ReflectionJdbcDaoException;
import Network.Messages.DatabaseMessages.MessageCreateUser;
import Network.Messages.DatabaseMessages.MessageGetQueueForUser;
import Network.Messages.DatabaseMessages.MessageNewPost;
import Network.Messages.MessageUnknown;
import Network.Messages.YarikMessage;
import Network.StandardQuad;
import abstractttt.AbstractServer;
import org.json.simple.JSONObject;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Created by sergeybp on 21.11.16.
 */
public class DatabaseServer extends AbstractServer {

    private ReflectionJdbcDao dao;

    public DatabaseServer(StandardQuad quad) {
        super(quad);

        DataSource dataSource = DatabaseNetworkUtils.getDataSource();
        DAOFactory.createFactory(dataSource);
        DAOFactory daoFactory = DAOFactory.getInstance();

        dao = daoFactory.getReflectionJdbcDao();
    }

    @Override
    public void handleReceive(JSONObject object) {
        YarikMessage gotMessage = new MessageUnknown();

        try {
            gotMessage = gotMessage.decode(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (gotMessage.getMessageType()) {
            case CREATE_USER:
                createUser(gotMessage);
                break;
            case NEW_POST:
                newPost(gotMessage);
                break;
            case GET_QUEUE_FOR_USER:
                getQueueForUser(gotMessage);
                break;
        }
    }

    private User getUser(String info) {
        User user = new User();

        String[] splitInfo = info.split("\\$");

        user.setName(splitInfo[0]);
        user.setInfo(info);

        try {
            return dao.getUser(user.getName());
        } catch (ReflectionJdbcDaoException e) {
            return null;
        }
    }

    private boolean registerUser(String info, String tags) {
        User user = new User();
        String[] splitInfo = info.split("\\$");

        user.setName(splitInfo[0]);
        user.setInfo(info);

        try {
            dao.insertUser(user, getTags(tags));
        } catch (ReflectionJdbcDaoException e) {
            return false;
        }

        return true;
    }

    private ArrayList<Tag> getTags(String tags) {
        String[] splitTags = tags.split("$");
        ArrayList<Tag> tagArrayList = new ArrayList<>();
        for (String tag: splitTags) tagArrayList.add(new Tag(tag));
        return tagArrayList;
    }

    private boolean updateInfoUser(User user, String tags) {
        try {
            dao.updateUser(user, getTags(tags));
        } catch (ReflectionJdbcDaoException e) {
            return false;
        }
        return true;
    }

    public boolean receiveFeedback(String messagesIds, String likes) {
        String[] messages = messagesIds.split("\\$");
        String[] like = likes.split("\\$");

        for (int i = 0; i < messages.length; i++) {
            Integer messageId = Integer.parseInt(messages[i]);
            Integer up = like[i].equals("+") ? 1 : -1;
            try {
                Message message = dao.getMessage(messageId);
                message.setRate(message.getRate() + up);
                dao.updateMessage(message);
            } catch (ReflectionJdbcDaoException e) {
                return false;
            }
        }
        return true;
    }

    private boolean postMessage(String info, String post, String tags) {
        User user = new User();
        String[] splitInfo = info.split("\\$");
        Message message = new Message(post, dao.getUserIdByName(splitInfo[0]), 0);
        dao.insertMessage(message, getTags(tags));
        return true;
    }

    private String getMessageQueue(Integer userId, Integer lastRead) {
        ArrayList<Message> preQueue = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>(dao.selectAllMessages());
        for (Message now : messages) {
            if (now.getId() < lastRead)
                continue;

            HashSet<Tag> userTags = new HashSet<>(dao.getUserTags(userId));
            HashSet<Tag> messageTags = new HashSet<>(dao.getMessageTags(now.getId()));

            userTags.removeAll(messageTags);
            if (!userTags.isEmpty()) {
                preQueue.add(now);
            }
        }
        Collections.sort(preQueue);
        int bound = Math.min(preQueue.size(), 20);
        StringBuilder result = new StringBuilder();
        for (Message toSend: preQueue.subList(0, bound)) {
            result.append(toSend.getId()).append("$").
                    append(toSend.getText()).append("$").
                    append(toSend.getRate()).append("$").
                    append(toSend.getUserId()).append("(").
                    append(dao.getMessageTags(
                            toSend.getId()).
                            stream().
                            map(Tag::getTag).
                            collect(Collectors.joining("$"))).
                    append(")").append("^");
        }
        return result.toString();
    }

    private ArrayList<String> createUser(YarikMessage gotMessage) {
        StandardQuad backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());

        String info = gotMessage.getMessageContent().get(2).getValue();
        String tags = gotMessage.getMessageContent().get(3).getValue();

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

        YarikMessage message = new MessageCreateUser();

        message.setFieldsContent(content);

        JSONObject encodedMessage = message.encode();
        sendJSON(backAddress.ip, backAddress.port, encodedMessage);
        return content;
    }

    private ArrayList<String> newPost(YarikMessage gotMessage) {
        StandardQuad backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());

        String info = gotMessage.getMessageContent().get(2).getValue();
        String post = gotMessage.getMessageContent().get(3).getValue();
        String tags = gotMessage.getMessageContent().get(4).getValue();

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add(gotMessage.getMessageContent().get(1).getValue());
        content.add(gotMessage.getMessageContent().get(2).getValue());

        if (postMessage(info, post, tags)) {
            content.add("OK");
            content.add("OK");
        } else {
            content.add("NO");
            content.add("NO");
        }
        MessageNewPost message = new MessageNewPost();
        message.setFieldsContent(content);

        JSONObject encodedMessage = message.encode();
        sendJSON(backAddress.ip, backAddress.port, encodedMessage);
        return content;
    }

    private ArrayList<String> getQueueForUser(YarikMessage gotMessage) {
        StandardQuad backAddress = new StandardQuad(gotMessage.getMessageContent().get(0).getValue());
        MessageGetQueueForUser message = new MessageGetQueueForUser();

        ArrayList<String> content = new ArrayList<>();
        content.add(myQuad.toString());
        content.add(gotMessage.getMessageContent().get(1).getValue());
        content.add(gotMessage.getMessageContent().get(2).getValue());
        content.add(gotMessage.getMessageContent().get(3).getValue());

        String info = gotMessage.getMessageContent().get(2).getValue();
        User user = getUser(info);

        if (user == null) {
            content.add("NO");
        } else {
            content.add(getMessageQueue(user.getId(),
                    Integer.parseInt(gotMessage.getMessageContent().get(3).getValue())));
        }

        message.setFieldsContent(content);
        JSONObject encodedMessage = message.encode();
        sendJSON(backAddress.ip, backAddress.port, encodedMessage);
        return content;
    }





}
