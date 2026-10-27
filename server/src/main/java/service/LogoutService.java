package service;

import requestresult.LogoutRequest;
import requestresult.LogoutResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public LogoutResult logoutUser(LogoutRequest logoutRequest) throws DataAccessException {
        AuthData authData = authDAO.getAuth(logoutRequest.authToken());

        if (authData == null) {
            throw new UnauthorizedException();
        }
        authDAO.deleteAuth(authData);

        return new LogoutResult();
    }

}
