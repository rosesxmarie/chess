package service;

import RequestResult.ListGamesRequest;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class ListGamesService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ArrayList<GameData> listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        AuthData authData = authDAO.getAuth(listGamesRequest.authToken());

        if (authData == null) {
            throw new UnauthorizedException();
        }

        return gameDAO.listGames();
    }
}
