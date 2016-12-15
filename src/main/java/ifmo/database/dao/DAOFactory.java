package ifmo.database.dao;

import javax.sql.DataSource;

/**
 * Factory that produces ifmo.Database.dao.ReflectionJdbcDao implementation
 */
public class DAOFactory {
    public static DAOFactory daoFactory;
    private DataSource dataSource;

    /**
     * Constructor for ifmo.Database.dao.DAOFactory class
     *
     * @param dataSource {@link DataSource} of ifmo.Database to connect to
     */
    private DAOFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates ifmo.Database.dao.DAOFactory class static
     *
     * @param dataSource {@link DataSource} of ifmo.Database to connect to
     */
    public static void createFactory(DataSource dataSource) {
        daoFactory = new DAOFactory(dataSource);
    }

    /**
     * Returns {@link DataSource} of ifmo.Database to connect to
     *
     * @return {@link DataSource} of ifmo.Database to connect to
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
     * @return implementation of {@link ReflectionJdbcDao}
     */
    public ReflectionJdbcDao getReflectionJdbcDao() {
        return new ReflectionJdbcDaoImpl();
    }
}
