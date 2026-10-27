package handler;

import RequestResult.CreateGameRequest;
import RequestResult.ListGamesRequest;
import RequestResult.LoginRequest;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import RequestResult.LogoutRequest;

import service.LogoutService;

public class LogoutHandler {
    private final LogoutService logoutService;

    public LogoutHandler(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    public String logoutUser(String authToken) throws DataAccessException {
        var serializer = new Gson();
        var logoutRequest = new LogoutRequest(authToken);
        var logoutResult = logoutService.logoutUser(logoutRequest);
        return serializer.toJson(logoutResult);
    }
}
