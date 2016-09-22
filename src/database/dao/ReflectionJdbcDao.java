package database.dao;

import java.util.List;

/**
 * Data access object for mapping class of some format (e.g. like JavaBean) to the table of the relational database
 *
 * @param <T> type of objects
 */
public interface ReflectionJdbcDao<T> {
    /**
     * Insert object to the table of the database
     *
     * @param object object that is inserted to a record in the database
     */
    public void insert(T object);

    /**
     * Update the record in the table of the database that is associated to the object.
     * Identification of the records is processing by key fields of the object
     *
     * @param object fields of this object are updated on the record in the table of the database
     */
    public void update(T object);

    /**
     * Delete the object from the table of the database. Identification of records is processing by key fields of the object.
     * Other fields of the object aren't used in this method and are able to be not filled.
     *
     * @param key key with filled key fields to delete
     */
    public void deleteByKey(T key);

    /**
     * Select the object from the table of the database. Identification is processing by key fields of the object.
     * Other fields of the object aren't used in this method and are able to be not filled.
     *
     * @param key object with filled key fields to select
     * @return object that is selected from the table of the database with filled all fields.
     */
    public T selectByKey(T key);

    /**
     * Select all objects from the table of the database.
     *
     * @return a list of selected objects
     */
    public List<T> selectAll();
}
