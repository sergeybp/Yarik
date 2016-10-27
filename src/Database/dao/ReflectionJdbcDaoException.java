package Database.dao;

/**
 * Exception that may {@link ReflectionJdbcDao} produce
 */
public class ReflectionJdbcDaoException extends RuntimeException {
    public ReflectionJdbcDaoException() {
        super();
    }

    public ReflectionJdbcDaoException(String message) {
        super(message);
    }

    public ReflectionJdbcDaoException(Throwable throwable) {
        super(throwable);
    }

    public ReflectionJdbcDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
