package ifmo.database.dao;

import ifmo.database.columns.Message;
import ifmo.database.columns.Tag;
import ifmo.database.columns.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.TestCaseId;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nikita on 15.12.16.
 */
@Features(value = "DAO implementation testing")
public class ReflectionJdbcDaoImplTest extends UnitTestBase {

    private ReflectionJdbcDao dao = null;

    @Before
    public void setUp() {
        DAOFactory.createFactory(mockDataSource());
        DAOFactory daoFactory = DAOFactory.getInstance();
        dao = daoFactory.getReflectionJdbcDao();
    }

    @Test
    @TestCaseId(value = "A1")
    public void testInsertTag() {
        dao.insertTag(new Tag("tag"));
    }

    @Test
    @TestCaseId(value = "A2")
    public void testInsertUser() {
        dao.insertUser(new User(1), Arrays.asList(new Tag("tag1"), new Tag("tag2")));
    }

    @Test(expected = ReflectionJdbcDaoException.class)
    @TestCaseId(value = "A3")
    public void testInsertMessage() {
        dao.insertMessage(new Message("text", 1, 0), Arrays.asList(new Tag("tag1"), new Tag("tag2")));
    }

    @Test
    @TestCaseId(value = "A4")
    public void testUpdateMessage() {
        dao.updateMessage(new Message("text1", 1, 0));
    }

    @Test
    @TestCaseId(value = "A5")
    public void testSelectAllMessages() {
        List<Message> list = dao.selectAllMessages();
        Assert.assertTrue("List must be empty", list.isEmpty());
    }

    @Test
    @TestCaseId(value = "A6")
    public void testGetMessageTags() {
        List<Tag> list = dao.getMessageTags(0);
        Assert.assertTrue("List must be empty", list.isEmpty());
    }

    @Test
    @TestCaseId(value = "A7")
    public void testGetUserTags() {
        List<Tag> list = dao.getUserTags(0);
        Assert.assertTrue("List must be empty", list.isEmpty());
    }

    @Test
    @TestCaseId(value = "A8")
    public void testGetUser() {
        User user = dao.getUser(0);
    }
}