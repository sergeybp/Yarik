package ifmo.database.dao;

import ifmo.database.columns.Message;
import ifmo.database.columns.Tag;
import ifmo.database.columns.User;

import java.util.List;

public interface ReflectionJdbcDao {
    Integer getUserIdByName(String name);
    void insertUser(User user, List<Tag> tags);
    void insertMessage(Message message, List<Tag> tags);
    void updateMessage(Message message);
    List<Tag> getMessageTags(Integer messageId);
    List<Tag> getUserTags(Integer userId);
    List<Message> selectAllMessages();
    Message getMessage(Integer id);
    void insertTag(Tag tag);
    User getUser(Integer id);
    User getUser(String name);
    void updateUser(User user, List<Tag> tags);

}
