package service;

import RequestResult.LoginRequest;
import RequestResult.LogoutRequest;
import RequestResult.RegisterRequest;
import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    @Test
    void logoutUser() throws DataAccessException {

        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        AuthData registerResult = registerService.registerUser(registerRequest);

        LoginService loginService = new LoginService(userDAO, authDAO);
        LoginRequest loginRequest = new LoginRequest("test", "test123");
        AuthData loginResult = loginService.loginUser(loginRequest);

        LogoutService logoutService = new LogoutService(authDAO);
        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        logoutService.logoutUser(logoutRequest);

        assertNull(authDAO.getAuth(loginResult.authToken()));

    }
}