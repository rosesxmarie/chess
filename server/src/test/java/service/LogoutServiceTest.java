package service;

import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.LogoutRequest;
import requestresult.RegisterRequest;
import dataaccess.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    @Test
    void logoutUser() throws DataAccessException {
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        registerService.registerUser(registerRequest);

        LoginService loginService = new LoginService(userDAO, authDAO);
        LoginRequest loginRequest = new LoginRequest("test", "test123");
        LoginResult loginResult = loginService.loginUser(loginRequest);

        LogoutService logoutService = new LogoutService(authDAO);
        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        logoutService.logoutUser(logoutRequest);

        assertNull(authDAO.getAuth(loginResult.authToken()));
    }

    @Test
    void unauthorizedUser() throws DataAccessException{
        LogoutService logoutService = new LogoutService(authDAO);
        LogoutRequest logoutRequest = new LogoutRequest("fakeToken");

        assertThrows(UnauthorizedException.class, () -> logoutService.logoutUser(logoutRequest));
    }
}