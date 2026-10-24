package service;

import RequestResult.CreateGameRequest;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;


public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private int newID = 1;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public GameData createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        AuthData authData = authDAO.getAuth(createGameRequest.authToken());

        if (authData == null) {
            throw new UnauthorizedException();
        }

        if (createGameRequest.gameName() == null) {
            throw  new BadRequestException();
        }

        int gameID = newID++;
        GameData gameData = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
        gameDAO.createGame(gameData);


        return gameData;
    }
}
