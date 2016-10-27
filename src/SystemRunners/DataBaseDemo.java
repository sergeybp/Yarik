package SystemRunners;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import Database.columns.User;
import Database.dao.DAOFactory;
import Database.dao.ReflectionJdbcDao;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseDemo {


    private static DataSource getDataSource() {
        Properties properties = new Properties();
        FileInputStream inputStream;
        MysqlDataSource mysqlDataSource = null;
        try {
            inputStream = new FileInputStream("db.properties");
            properties.load(inputStream);
            mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            mysqlDataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            mysqlDataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDataSource;
    }

    public static void main(String[] args) {
        ReflectionJdbcDao<User> reflectionJdbcDao;
        DataSource dataSource = null;

        dataSource = getDataSource();
        DAOFactory.createFactory(dataSource);
        DAOFactory daoFactory = DAOFactory.getInstance();
        reflectionJdbcDao = daoFactory.getReflectionJdbcDao(User.class);
        Connection connection;
        try {
            connection = dataSource.getConnection();
            //try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Users")) {
            //  statement.executeUpdate();
            //}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User nikita = new User(0);
        nikita.setName("Nikita");
        nikita.setSurname("Markovnikov");
        nikita.setRate(100);
        reflectionJdbcDao.update(nikita);
    }
}
