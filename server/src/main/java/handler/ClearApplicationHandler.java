package handler;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

import service.ClearService;
import io.javalin.http.Context;



public class ClearApplicationHandler {

    private final ClearService clearService;

    public ClearApplicationHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clear(Context ctx) throws DataAccessException {
        clearService.clearApplication();
        ctx.result("{}");
    }
}
