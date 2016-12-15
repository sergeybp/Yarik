package ifmo.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by sergeybp on 27.10.16.
 */
public class DatabaseNetworkUtils {
    public static DataSource getDataSource() {
        Properties properties = new Properties();
        FileInputStream inputStream;
        MysqlDataSource mysqlDataSource = null;
        try {
            inputStream = new FileInputStream(DatabaseNetworkUtils.class.getClassLoader().getResource("db.properties").getFile());
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


}
