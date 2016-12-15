package Database.dao;

import Database.columns.Message;
import Database.columns.Tag;
import Database.columns.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by nikita on 15.12.16.
 */
public class ReflectionJdbcDaoImplTest extends UnitTestBase {

    private ReflectionJdbcDao dao = null;

    @Before
    public void setUp() {
        DAOFactory.createFactory(mockDataSource());
        DAOFactory daoFactory = DAOFactory.getInstance();
        dao = daoFactory.getReflectionJdbcDao();
    }

    @Test
    public void testInsertTag() {
        dao.insertTag(new Tag("tag"));
    }

    @Test
    public void testInsertUser() {
        dao.insertUser(new User(1), Arrays.asList(new Tag("tag1"), new Tag("tag2")));
    }

    @Test(expected = ReflectionJdbcDaoException.class)
    public void testInsertMessage() {
        dao.insertMessage(new Message("text", 1, 0), Arrays.asList(new Tag("tag1"), new Tag("tag2")));
    }

    @Test
    public void testUpdateMessage() {
        dao.updateMessage(new Message("text1", 1, 0));
    }

    @Test
    public void testSelectAllMessages() {
        List<Message> list = dao.selectAllMessages();
        assertThat("List must be empty", list.isEmpty());
    }
}