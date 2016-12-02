package Database.dao;

import Database.columns.Message;
import Database.columns.Tag;
import Database.columns.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ReflectionJdbcDaoImpl implements ReflectionJdbcDao {

    protected ReflectionJdbcDaoImpl() {}

    @Override
    public Integer getUserIdByName(String name) {
        String query = "SELECT user_id FROM users WHERE name = '" + name + "';";
        return (Integer) getObject(query, "user_id");
    }

    private Integer getAutoIncrement(String table) {
        String query = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + table +
        "'AND table_schema = DATABASE( );";
        return (Integer) getObject(query, "AUTO_INCREMENT");
    }

    private Object getObject(String query, String column) {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        Object result = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            result = resultSet.getInt(column);
                        }
                    }
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Selecting object error");
        }
        return result;
    }

    private void insertUserTags(Integer userId, List<Tag> tags) {
        String query = "INSERT INTO users_tags(user_id, tag) values ";
        query += tags.stream().
                map(t -> "('" + userId + "','" + t.getTag() + "')").
                collect(Collectors.joining(","));
        insertQuery(query);
    }

    private void insertMessageTags(Integer messageId, Integer userId, List<Tag> tags) {
        String query = "INSERT INTO messages_tags(message_id, user_id, tag) values ";
        query += tags.stream().
                map(t -> "('" + messageId + "','" + userId + "','" + t.getTag() + "')").
                collect(Collectors.joining(","));
        insertQuery(query);
    }

    private void insertQuery(String sql) {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.execute();
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Error of inserting user");
        }

    }

    @Override
    public void insertUser(User user, List<Tag> tags) {
        String query = "INSERT INTO users(name, info, rate) values (" +
                "'" + user.getName() + "'," +
                "'" + user.getInfo() + "'," +
                "'" + user.getRate() + "');";
        insertQuery(query);
        insertUserTags(getUserIdByName(user.getName()), tags);
    }

    @Override
    public void insertMessage(Message message, List<Tag> tags) {
        String query = "INSERT INTO messages(text, rate, user_id) values (" +
                "'" + message.getText() + "'," +
                "'" + message.getRate() + "'," +
                "'" + message.getUserId() + "')";
        insertQuery(query);
        Integer messageId = getAutoIncrement("messages");
        insertMessageTags(messageId - 1, message.getUserId(), tags);
    }

    @Override
    public void insertTag(Tag tag) {
        String query = "INSERT INTO tags(tag) values ('" + tag.getTag() + "');";
        insertQuery(query);
    }

    @Override
    public User getUser(Integer userId) {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        User user = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                String query = "SELECT * FROM users WHERE user_id = '" + userId + "';";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        user = new User();
                        if (resultSet.next()) {
                            user.setId(resultSet.getInt("user_id"));
                            user.setName(resultSet.getString("name"));
                            user.setInfo(resultSet.getString("info"));
                            user.setRate(resultSet.getInt("rate"));
                        }
                    }
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Selecting object error");
        }
        return user;
    }

    @Override
    public User getUser(String name) {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        User user = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                String query = "SELECT * FROM users WHERE name = '" + name + "';";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        user = new User();
                        if (resultSet.next()) {
                            user.setId(resultSet.getInt("user_id"));
                            user.setName(resultSet.getString("name"));
                            user.setInfo(resultSet.getString("info"));
                            user.setRate(resultSet.getInt("rate"));
                        }
                    }
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Selecting object error");
        }
        return user;
    }

    private void update(String query) {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.executeUpdate(query);
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Updating error");
        }
    }

    @Override
    public void updateUser(User user, List<Tag> tags) {
        String query = String.format("UPDATE users SET name = '%s', info = '%s', rate = '%d' WHERE user_id  = '%d'",
                user.getName(), user.getInfo(), user.getRate(), user.getId());
        update(query);
        query = "DELETE FROM users_tags WHERE user_id = '" + user.getId() + "'";
        update(query);
        insertUserTags(user.getId(), tags);
    }

    @Override
    public void updateMessage(Message message) {
        String query = String.format("UPDATE messages SET text = '%s', rate = '%d' WHERE message_id  = '%d'",
                message.getText(),
                message.getRate(),
                message.getId()
        );
        update(query);
    }

    @Override
    public Message getMessage(Integer messageId) {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        Message message = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                String query = "SELECT * FROM messages WHERE user_id = '" + messageId + "';";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        message = new Message();
                        if (resultSet.next()) {
                            message.setId(resultSet.getInt("message_id"));
                            message.setText(resultSet.getString("text"));
                            message.setRate(resultSet.getInt("rate"));
                            message.setUserId(resultSet.getInt("user_id"));
                        }
                    }
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Selecting object error");
        }
        return message;
    }

    @Override
    public List<Message> selectAllMessages() {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        ArrayList<Message> messages = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                String query = "SELECT * FROM messages;";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        messages = new ArrayList<>();
                        while (resultSet.next()) {
                            Message message = new Message();
                            message.setId(resultSet.getInt("message_id"));
                            message.setText(resultSet.getString("text"));
                            message.setRate(resultSet.getInt("rate"));
                            message.setUserId(resultSet.getInt("user_id"));
                            messages.add(message);
                        }
                    }
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Selecting object error");
        }
        return messages;
    }

    public List<Tag> getUserTags(Integer userId) {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        ArrayList<Tag> tags = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                String query = "SELECT * FROM users_tags WHERE user_id = '" + userId + "'";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        tags = new ArrayList<>();
                        while (resultSet.next())
                            tags.add(new Tag(resultSet.getString("tag")));
                    }
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Selecting object error");
        }
        return tags;
    }

    public List<Tag> getMessageTags(Integer messageId) {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        ArrayList<Tag> tags = null;
        try {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                String query = "SELECT * FROM messages_tags WHERE message_id = '" + messageId + "';";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        tags = new ArrayList<>();
                        while (resultSet.next()) {
                            tags.add(new Tag(resultSet.getString("tag")));
                        }
                    }
                    connection.commit();
                    preparedStatement.close();
                }
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Selecting object error");
        }
        return tags;
    }
}
