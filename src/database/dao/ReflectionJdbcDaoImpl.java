package database.dao;

import database.annotations.Column;
import database.annotations.KeyColumn;
import database.annotations.Table;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link ReflectionJdbcDao} interface. Works with MySQL databases.
 *
 * @param <T> a type of objects that are located in the table of the database
 */
public class ReflectionJdbcDaoImpl<T> implements ReflectionJdbcDao<T> {
    private Class<T> clazz;

    /**
     * Class constructor
     *
     * @param clazz Class of type T to get this type
     */
    protected ReflectionJdbcDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(T object) {
        try {
            insertObject(object);
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Error of insertion of object from " + object.getClass().getName(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(T object) {
        try {
            updateObject(object);
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Error of updating of object from" + object.getClass().getName(), e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByKey(T key) {
        try {
            deleteObject(key);
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Error of deleting of object from" + key.getClass().getName(), e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T selectByKey(T key) {
        try {
            return executeSelect(key);
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Error of deleting of object from" + key.getClass().getName(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> selectAll() {
        try {
            return executeSelectAll();
        } catch (SQLException e) {
            throw new ReflectionJdbcDaoException("Error of selecting all objects", e);
        }
    }

    /**
     * Inserts object to the table of the database
     *
     * @param object object to insert
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private void insertObject(T object) throws SQLException {
        List<Field> fields = getFields(object);
        String tableName = getTableName(object);
        String columns = fields.stream().map(this::getColumnName).collect(Collectors.joining(","));
        String values = fields.stream().map(x -> "?").collect(Collectors.joining(","));
        String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        executeUpdate(query, fields, object);
    }

    /**
     * Updates object to the table of the database
     *
     * @param object object to update
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private void updateObject(T object) throws SQLException {
        List<Field> keys = getKeyFields(object);
        List<Field> fields = getFields(object);
        String tableName = getTableName(object);
        String values = fields.stream().map(x -> getColumnName(x) + "=?").collect(Collectors.joining(","));
        String condition = keys.stream().map(x -> getColumnName(x) + "=?").collect(Collectors.joining(","));
        String query = "UPDATE " + tableName + " SET " + values + " WHERE (" + condition + ")";
        executeUpdate(query, Stream.concat(fields.stream(), keys.stream()).collect(Collectors.toList()), object);
    }

    /**
     * Deletes object to the table of the database
     *
     * @param object object to delete
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private void deleteObject(T object) throws SQLException {
        List<Field> keys = getKeyFields(object);
        String tableName = getTableName(object);
        String condition = keys.stream().map(x -> getColumnName(x) + "=?").collect(Collectors.joining(","));
        String query = "DELETE FROM " + tableName + " WHERE (" + condition + ")";
        executeUpdate(query, keys, object);
    }

    /**
     * Selects object from the table by specified key
     *
     * @param object key to select by
     * @return selects object
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private T executeSelect(T object) throws SQLException {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            List<Field> keys = getKeyFields(object);
            List<Field> fields = getFields(object);
            String tableName = getTableName(object);
            String condition = keys.stream().map(x -> getColumnName(x) + "=?").collect(Collectors.joining(","));
            String query = "SELECT * FROM " + tableName + " WHERE (" + condition + ")";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                prepareStatement(preparedStatement, keys, object);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        for (Field field : fields) {
                            String column = getColumnName(field);
                            setValue(object, field, convert(field, column, resultSet));
                        }
                    }
                }
                connection.commit();
                preparedStatement.close();
            }
        } finally {
            if (connection != null)
                connection.close();
        }
        return object;
    }

    /**
     * Select all objects from the table
     *
     * @return {@link List}
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private List<T> executeSelectAll() throws SQLException {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        ArrayList<T> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            List<Field> fields = getFields(clazz);
            String tableName = clazz.getAnnotation(Table.class).table();
            fields.addAll(getKeyFields(clazz));
            String query = "SELECT * FROM " + tableName;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        T object = clazz.newInstance();
                        for (Field field : fields) {
                            String column = getColumnName(field);
                            setValue(object, field, convert(field, column, resultSet));
                        }
                        result.add(object);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new SQLException(e);
                }
                connection.commit();
                preparedStatement.close();
            }
        } finally {
            if (connection != null)
                connection.close();
        }
        return result;
    }

    /**
     * Updates record of the object in the table by using query and filled fields
     *
     * @param query  mysql query that is using update the record
     * @param fields fields that contain data for updating
     * @param object record of this object updates
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private void executeUpdate(String query, List<Field> fields, T object) throws SQLException {
        DataSource dataSource = DAOFactory.getInstance().getDataSource();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                prepareStatement(preparedStatement, fields, object);
                preparedStatement.executeUpdate();
            }
        }
    }

    /**
     * Converts data from fields to Java objects
     *
     * @param field     {@link Field} to convert
     * @param column    column from which data taking from {@link ResultSet}
     * @param resultSet {@link ResultSet} from whick data is taking
     * @return converted object
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private Object convert(Field field, String column, ResultSet resultSet) throws SQLException {
        if (field.getType().equals(Date.class))
            return new Date(((Timestamp) resultSet.getObject(column)).getTime());
        else if (field.getType().equals(boolean.class))
            return resultSet.getBoolean(column);
        else {
            try {
                String value = resultSet.getString(column);
                if (value != null)
                    return field.getType().getConstructor(String.class).newInstance(value.trim());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     * Fills specified {@link PreparedStatement} with data
     *
     * @param statement {@link PreparedStatement} to fill
     * @param fields    {@link Field}s from which data is taking
     * @param object    key for taking data
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private void prepareStatement(PreparedStatement statement, List<Field> fields, T object) throws SQLException {
        int i = 1;
        for (Field field : fields) {
            if (field.getType() == Date.class) {
                long date = ((Date) getValue(object, field)).getTime();
                statement.setTimestamp(i, new Timestamp(date));
            } else statement.setObject(i, getValue(object, field));
            i++;
        }
    }

    /**
     * Invokes data from field by object
     *
     * @param object is used to ivoke data
     * @param field  {@link Field} from which data is taking
     * @return invoked object
     * @throws SQLException throws in case of exceptions in connecting of accessing to the database
     */
    private Object getValue(T object, Field field) throws SQLException {
        try {
            return new PropertyDescriptor(field.getName(), object.getClass()).getReadMethod().invoke(object);
        } catch (InvocationTargetException | IntrospectionException | IllegalAccessException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Set value to the field by object
     *
     * @param object data invoked by this object
     * @param field  {@link Field} from which data is taking
     * @param value  {@link Object} to set
     * @throws SQLException throws in case of bad accessing to the database
     */
    private void setValue(T object, Field field, Object value) throws SQLException {
        try {
            new PropertyDescriptor(field.getName(), object.getClass()).getWriteMethod().invoke(object, value);
        } catch (InvocationTargetException | IntrospectionException | IllegalAccessException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Returns name of column by which field is annotated
     *
     * @param field choosing by this field
     * @return name of the column
     */
    private String getColumnName(Field field) {
        String columnName = null;
        for (Annotation annotation : field.getDeclaredAnnotations())
            if (annotation.annotationType().equals(Column.class))
                columnName = field.getAnnotation(Column.class).column();
        return columnName;
    }

    /**
     * Returns the name of table of types as object
     *
     * @param object types of this object is being taken
     * @return name of the table
     */
    private String getTableName(T object) {
        return object.getClass().getAnnotation(Table.class).table();
    }

    /**
     * Returns {@link List} of {@link Field}s by database.annotations of the object
     *
     * @param object is used to take fields
     * @return {@link List} of {@link Field}s that was annotated
     */
    private List<Field> getFields(T object) {
        return getFieldsByAnnotations(object, new HashSet<>(Collections.singletonList(Column.class)));
    }

    /**
     * Returns {@link List} of {@link Field}s by database.annotations of the object
     *
     * @param clazz is used to take fields
     * @return {@link List} of {@link Field}s that was annotated
     */
    private List<Field> getFields(Class<?> clazz) {
        return getFieldsByAnnotations(clazz, new HashSet<>(Collections.singletonList(Column.class)));
    }

    /**
     * Returns {@link List} of key {@link Field}s by database.annotations of the object
     *
     * @param object is used to take kay fields
     * @return {@link List} of key {@link Field}s that was annotated
     */
    private List<Field> getKeyFields(T object) {
        return getFieldsByAnnotations(object, new HashSet<>(Collections.singletonList(KeyColumn.class)));
    }

    /**
     * Returns {@link List} of key  {@link Field}s by database.annotations of the class
     *
     * @param clazz is used to take key fields
     * @return {@link List} of key {@link Field}s that was annotated
     */
    private List<Field> getKeyFields(Class<?> clazz) {
        return getFieldsByAnnotations(clazz, new HashSet<>(Collections.singletonList(KeyColumn.class)));
    }

    /**
     * Returns {@link List} of {@link Field}s by specified database.annotations of the object
     *
     * @param object      is used to take fields
     * @param annotations database.annotations that are used to take fields
     * @return {@link List} of {@link Field}s that was annotated
     */
    private List<Field> getFieldsByAnnotations(T object, Set<? extends Class<? extends Annotation>> annotations) {
        return getFieldsByAnnotations(object.getClass(), annotations);
    }

    /**
     * Returns {@link List} of {@link Field}s by specified database.annotations of the class
     *
     * @param clazz       is used to take fields
     * @param annotations database.annotations that are used to take fields
     * @return {@link List} of {@link Field}s that was annotated
     */
    private List<Field> getFieldsByAnnotations(Class<?> clazz, Set<? extends Class<? extends Annotation>> annotations) {
        List<Field> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            Set<Class<? extends Annotation>> temp = new HashSet<>();
            for (Annotation annotation : field.getDeclaredAnnotations()) temp.add(annotation.annotationType());
            temp.retainAll(annotations);
            if (!temp.isEmpty()) result.add(field);
        }
        return result;
    }

}
