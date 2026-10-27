package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import RequestResult.CreateGameRequest;
import service.CreateGameService;

public class CreateGameHandler {
    private final CreateGameService createGameService;

    public CreateGameHandler(CreateGameService createGameService) {
        this.createGameService = createGameService;
    }

    public String createGame(String authToken, String gameName) throws DataAccessException {
        var serializer = new Gson();
        var request = serializer.fromJson(gameName, CreateGameRequest.class);
        var createGameRequest = new CreateGameRequest(authToken, request.gameName());
        var createGameResult = createGameService.createGame(createGameRequest);
        return serializer.toJson(createGameResult);

    }
}
