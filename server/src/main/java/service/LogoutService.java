package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logoutUser(LogoutRequest logoutRequest) throws DataAccessException {
        AuthData authData = authDAO.getAuth(logoutRequest.authToken());

        if (authData == null) {
            throw new UnauthorizedException();
        }
        authDAO.deleteAuth(authData);
    }

}
