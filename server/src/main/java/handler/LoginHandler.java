package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.LoginService;
import RequestResult.*;

public class LoginHandler {
    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    public String login(String request) throws DataAccessException {
        var serializer = new Gson();
        var loginRequest = serializer.fromJson(request, LoginRequest.class);
        var loginResult = loginService.loginUser(loginRequest);
        return serializer.toJson(loginResult);
    }
}
