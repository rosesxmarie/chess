package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.RegisterService;


import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    @Test
    void registerUser() throws Exception {
        var service = new RegisterService(new MemoryUserDAO(), new MemoryAuthDAO());
        var registerRequest = new UserData("test", "test123", "test@byu.edu");
        var registerResult = service.registerUser(registerRequest);

        assertEquals("test",  registerRequest.username());
        assertNotNull(registerResult.authToken());
        // Test for already taken user
        assertThrows(Exception.class, () -> service.registerUser(new UserData("test", "test123", "test@byu.edu")));
    }
}