package Database.dao;

import javax.sql.DataSource;

/**
 * Factory that produces Database.dao.ReflectionJdbcDao implementation
 */
public class DAOFactory {
    public static DAOFactory daoFactory;
    private DataSource dataSource;

    /**
     * Constructor for Database.dao.DAOFactory class
     *
     * @param dataSource {@link DataSource} of Database to connect to
     */
    private DAOFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates Database.dao.DAOFactory class static
     *
     * @param dataSource {@link DataSource} of Database to connect to
     */
    public static void createFactory(DataSource dataSource) {
        daoFactory = new DAOFactory(dataSource);
    }

    /**
     * Returns {@link DataSource} of Database to connect to
     *
     * @return {@link DataSource} of Database to connect to
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Returns instance of factory
     *
     * @return {@link DAOFactory}'s instance
     */
    public static DAOFactory getInstance() {
        return daoFactory;
    }

    /**
     * Returns implementation of DAO
     *
     * @param clazz Class for getting type
     * @param <T>   Type of objects that locate in the table of Database
     * @return implementation of {@link ReflectionJdbcDao}
     */
    public <T> ReflectionJdbcDao<T> getReflectionJdbcDao(Class<T> clazz) {
        return new ReflectionJdbcDaoImpl<>(clazz);
    }
}
