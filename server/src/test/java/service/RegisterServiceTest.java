package service;

import RequestResult.RegisterRequest;
import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    @Test
    void registerUser() throws DataAccessException {
        RegisterService service = new RegisterService(new MemoryUserDAO(), new MemoryAuthDAO());
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        AuthData registerResult = service.registerUser(registerRequest);

        assertEquals("test",  registerResult.username());
        assertNotNull(registerResult.authToken());
    }

    @Test
    void alreadyTaken() throws DataAccessException {
        RegisterService service = new RegisterService(new MemoryUserDAO(), new MemoryAuthDAO());
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        service.registerUser(registerRequest);

        assertThrows(DataAccessException.class, () -> service.registerUser(new RegisterRequest("test", "test123", "test@byu.edu")));

    }

    @Test
    void BadRequest() throws DataAccessException{
        RegisterService service = new RegisterService(new MemoryUserDAO(), new MemoryAuthDAO());
        RegisterRequest badRequest = new RegisterRequest(null, "test123", "test@byu.edu");

        assertThrows(DataAccessException.class, () -> service.registerUser(badRequest));
    }
}