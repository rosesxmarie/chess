package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.LogoutRequest;

import service.LogoutService;

public class LogoutHandler {
    private final LogoutService logoutService;

    public LogoutHandler(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    public void logoutUser(Context ctx) throws DataAccessException {
        var logoutRequest = new LogoutRequest(ctx.header("authorization"));
        logoutService.logoutUser(logoutRequest);
        ctx.result("{}");
    }
}
