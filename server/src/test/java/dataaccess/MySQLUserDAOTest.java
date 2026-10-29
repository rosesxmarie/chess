package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class MySQLUserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        try {
            userDAO = new MySQLUserDAO();
            userDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUserValid() throws DataAccessException {
        assertDoesNotThrow(() -> userDAO.createUser(new UserData("test", "test123", "test@byu.edu")));
    }

    @Test
    void createUserInvalid() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData(null, "test123", "test123@byu.edu")));
    }

    @Test
    void getUserValid() throws DataAccessException {
        userDAO.createUser(new UserData("test", "test123", "test@byu.edu"));

        UserData user = userDAO.getUser("test");
        assertEquals("test", user.username());
    }

    @Test
    void getUserInvalid() throws DataAccessException {
        userDAO.createUser(new UserData("test", "test123", "test@byu.edu"));
        assertNull(userDAO.getUser("test1"));
    }

    @Test
    void clear() throws DataAccessException {
        userDAO.createUser(new UserData("test", "test123", "test@byu.edu"));
        userDAO.clear();
        assertNull(userDAO.getUser("test"));
    }
}