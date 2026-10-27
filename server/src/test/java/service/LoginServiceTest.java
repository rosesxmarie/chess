package service;

import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.RegisterRequest;
import dataaccess.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    @Test
    void loginUser() throws DataAccessException {
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        registerService.registerUser(registerRequest);

        LoginService loginService = new LoginService(userDAO, authDAO);
        LoginRequest loginRequest = new LoginRequest("test", "test123");
        LoginResult loginResult = loginService.loginUser(loginRequest);

        assertEquals("test",  loginResult.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void badRequest() throws DataAccessException{
        LoginService loginService = new LoginService(userDAO, authDAO);
        LoginRequest badUsernameRequest = new LoginRequest(null, "test123");
        LoginRequest badPasswordRequest = new LoginRequest("test", null);

        assertThrows(DataAccessException.class, () -> loginService.loginUser(badUsernameRequest));
        assertThrows(DataAccessException.class, () -> loginService.loginUser(badPasswordRequest));
    }

    @Test
    void unauthorizedUser() throws DataAccessException{
        LoginService loginService = new LoginService(userDAO, authDAO);
        LoginRequest loginRequest = new LoginRequest("test", "test123");

        assertThrows(UnauthorizedException.class, () -> loginService.loginUser(loginRequest));
    }
}