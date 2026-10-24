package handler;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import RequestResult.ListGamesRequest;
import service.ListGamesService;

public class ListGamesHandler {
    private final ListGamesService listGamesService;

    public ListGamesHandler(ListGamesService listGamesService) {
        this.listGamesService = listGamesService;
    }

    public void listGames(Context ctx) throws DataAccessException {
        var listGamesRequest = new ListGamesRequest(ctx.header("authorization"));
        listGamesService.listGames(listGamesRequest);
        ctx.result("{}");
    }
}
