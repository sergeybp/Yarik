package Database.dao;

import java.util.List;

/**
 * Data access object for mapping class of some format (e.g. like JavaBean) to the table of the relational Database
 *
 * @param <T> type of objects
 */
public interface ReflectionJdbcDao<T> {
    /**
     * Insert object to the table of the Database
     *
     * @param object object that is inserted to a record in the Database
     */
    public void insert(T object);

    /**
     * Update the record in the table of the Database that is associated to the object.
     * Identification of the records is processing by key fields of the object
     *
     * @param object fields of this object are updated on the record in the table of the Database
     */
    public void update(T object);

    /**
     * Delete the object from the table of the Database. Identification of records is processing by key fields of the object.
     * Other fields of the object aren't used in this method and are able to be not filled.
     *
     * @param key key with filled key fields to delete
     */
    public void deleteByKey(T key);

    /**
     * Select the object from the table of the Database. Identification is processing by key fields of the object.
     * Other fields of the object aren't used in this method and are able to be not filled.
     *
     * @param key object with filled key fields to select
     * @return object that is selected from the table of the Database with filled all fields.
     */
    public T selectByKey(T key);

    /**
     * Select all objects from the table of the Database.
     *
     * @return a list of selected objects
     */
    public List<T> selectAll();
}
