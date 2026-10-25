package service;

import RequestResult.CreateGameResult;
import RequestResult.JoinGameRequest;
import RequestResult.JoinGameResult;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
}
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        AuthData authData = authDAO.getAuth(joinGameRequest.authToken());

        if (joinGameRequest.authToken() == null || joinGameRequest.playerColor() == null || joinGameRequest.gameID() == 0) {
            throw new BadRequestException();
        }

        if (authData == null) {
            throw new UnauthorizedException();
        }

        GameData game = gameDAO.getGame(joinGameRequest.gameID());

        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();;

        if (joinGameRequest.playerColor().equals("WHITE")) {
            if (whiteUsername!= null) {
                throw new AlreadyTakenException();
            }
            whiteUsername = authData.username();
        } else if (joinGameRequest.playerColor().equals("BLACK")) {
            if (blackUsername != null) {
                throw  new AlreadyTakenException();
            }
            blackUsername = authData.username();
        } else {
            throw new BadRequestException();
        }

        gameDAO.updateGame(game.gameID(), new GameData(game.gameID(), whiteUsername, blackUsername, game.gameName(), game.game()));
        return new JoinGameResult();
    }
}
