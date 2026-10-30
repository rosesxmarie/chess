package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySQLAuthDAOTest {
    private AuthDAO authDAO;

    @BeforeEach
    void setUp() {
        try {
            authDAO = new MySQLAuthDAO();
            authDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clear() throws DataAccessException {
        authDAO.createAuth(new AuthData("authToken", "username"));
        authDAO.clear();
        assertNull(authDAO.getAuth("authToken"));
    }

    @Test
    void createAuthValid() throws DataAccessException {
        assertDoesNotThrow(() -> authDAO.createAuth(new AuthData("authToken", "username")));
    }

    @Test
    void createAuthInvalid() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(new AuthData(null, "username")));
    }

    @Test
    void getAuthValid() throws DataAccessException {
        authDAO.createAuth(new AuthData("authToken", "username"));
        AuthData auth = authDAO.getAuth("authToken");
        assertNotNull(auth);
        assertEquals("username", auth.username());
        assertEquals("authToken", auth.authToken());
    }

    @Test
    void getAuthInvalid() throws DataAccessException {
        authDAO.createAuth(new AuthData("authToken", "username"));
        assertNull(authDAO.getAuth("fakeToken"));
    }

    @Test
    void deleteAuth() throws DataAccessException {
        authDAO.createAuth(new AuthData("authToken", "username"));
        AuthData auth = authDAO.getAuth("authToken");
        authDAO.deleteAuth(auth);

        assertNull(authDAO.getAuth("authToken"));
    }
}