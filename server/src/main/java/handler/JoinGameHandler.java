package handler;

import RequestResult.JoinGameRequest;
import RequestResult.ListGamesRequest;
import RequestResult.LoginRequest;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.JoinGameService;
import service.ListGamesService;

public class JoinGameHandler {
    private final JoinGameService joinGameService;

    public JoinGameHandler(JoinGameService joinGameService) {
        this.joinGameService = joinGameService;
    }

    public String joinGame(String authToken, String json) throws DataAccessException {
        var serializer = new Gson();
        var body = serializer.fromJson(json, JoinGameRequest.class);
        var joinGameRequest = new JoinGameRequest(authToken, body.playerColor(), body.gameID());
        var joinGameResult = joinGameService.joinGame(joinGameRequest);
        return serializer.toJson(joinGameResult);
    }
}
