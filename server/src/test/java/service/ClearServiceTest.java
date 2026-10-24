package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import dataaccess.*;

class ClearServiceTest {
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    @Test
    void clearApplication() throws DataAccessException {

        RegisterService registerService = new RegisterService(userDAO, authDAO);
        ClearService clearService =  new ClearService(userDAO, authDAO, gameDAO);

        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        AuthData registerResult = registerService.registerUser(registerRequest);

        assertEquals("test",  registerResult.username());
        assertNotNull(userDAO.getUser("test"));
        clearService.clearApplication();

        assertNull(userDAO.getUser("test"));

    }
}