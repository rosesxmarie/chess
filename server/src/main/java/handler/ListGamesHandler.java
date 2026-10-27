package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requestresult.ListGamesRequest;
import service.ListGamesService;

public class ListGamesHandler {
    private final ListGamesService listGamesService;

    public ListGamesHandler(ListGamesService listGamesService) {
        this.listGamesService = listGamesService;
    }

    public String listGames(String authToken) throws DataAccessException {
        var serializer = new Gson();
        var listGamesRequest = new ListGamesRequest(authToken);
        var listGamesResult = listGamesService.listGames(listGamesRequest);
        return serializer.toJson(listGamesResult);
    }
}
