package service;

import RequestResult.ClearRequest;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import RequestResult.ClearResult;

public class ClearService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ClearService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this. gameDAO = gameDAO;
    }

    public ClearResult clearApplication() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

        return new ClearResult();
    }
}
