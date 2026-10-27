package service;

import RequestResult.RegisterRequest;
import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.Test;
import RequestResult.*;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    @Test
    void clearApplication() throws DataAccessException {
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        RegisterResult registerResult = registerService.registerUser(registerRequest);

        ClearService clearService =  new ClearService(userDAO, authDAO, gameDAO);

        assertEquals("test",  registerResult.username());
        assertNotNull(userDAO.getUser("test"));
        assertNotNull(authDAO.getAuth(registerResult.authToken()));

        clearService.clearApplication();

        assertNull(userDAO.getUser("test"));
        assertNull(authDAO.getAuth(registerResult.authToken()));

    }
}