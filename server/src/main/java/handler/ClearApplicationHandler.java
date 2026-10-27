package handler;
import RequestResult.ClearRequest;
import RequestResult.LogoutRequest;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

import service.ClearService;
import io.javalin.http.Context;



public class ClearApplicationHandler {

    private final ClearService clearService;

    public ClearApplicationHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public String clear() throws DataAccessException {
        var serializer = new Gson();
        var clearResult = clearService.clearApplication();
        return serializer.toJson(clearResult);
    }
}
