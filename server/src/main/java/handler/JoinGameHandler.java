package handler;

import requestresult.JoinGameRequest;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.JoinGameService;

public class JoinGameHandler {
    private final JoinGameService joinGameService;

    public JoinGameHandler(JoinGameService joinGameService) {
        this.joinGameService = joinGameService;
    }

    public String joinGame(String authToken, String request) throws DataAccessException {
        var serializer = new Gson();
        var body = serializer.fromJson(request, JoinGameRequest.class);
        var joinGameRequest = new JoinGameRequest(authToken, body.playerColor(), body.gameID());
        var joinGameResult = joinGameService.joinGame(joinGameRequest);
        return serializer.toJson(joinGameResult);
    }
}
