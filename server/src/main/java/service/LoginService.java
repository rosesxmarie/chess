package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData loginUser(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDAO.getUser(loginRequest.username());

        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException();
        }

        if (userData == null) {
            throw new UnauthorizedException();
        }

        if (!Objects.equals(userData.password(), loginRequest.password())) {
            throw new UnauthorizedException();
        }


        userDAO.getUser(loginRequest.username());
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, loginRequest.username());
        authDAO.createAuth(authData);

        return authData;
    }
}
