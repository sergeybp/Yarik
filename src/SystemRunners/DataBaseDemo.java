package SystemRunners;

import Database.columns.Tag;
import Database.columns.User;
import Database.dao.DAOFactory;
import Database.dao.ReflectionJdbcDao;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
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
        ReflectionJdbcDao reflectionJdbcDao;
        DataSource dataSource = null;

        dataSource = getDataSource();
        DAOFactory.createFactory(dataSource);
        DAOFactory daoFactory = DAOFactory.getInstance();
        reflectionJdbcDao = daoFactory.getReflectionJdbcDao();

        User user = new User();
        user.setName("ivan");
        user.setInfo("Booooom!");
        user.setRate(13);
        reflectionJdbcDao.insertUser(user, Arrays.asList(new Tag("Sport"), new Tag("Music")));
    }
}
