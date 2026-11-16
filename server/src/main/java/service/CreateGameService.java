package service;

import requestresult.CreateGameRequest;
import requestresult.CreateGameResult;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;


public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        AuthData authData = authDAO.getAuth(createGameRequest.authToken());

        if (authData == null) {
            throw new UnauthorizedException();
        }

        if (createGameRequest.gameName() == null) {
            throw  new BadRequestException();
        }

        int lastID = 0;
        for (GameData game : gameDAO.listGames()) {
            if (game.gameID() > lastID) {
                lastID = game.gameID();
            }
        }

        int gameID = lastID + 1;

        GameData gameData = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
        gameDAO.createGame(gameData);

        return new CreateGameResult(gameID);
    }
}
