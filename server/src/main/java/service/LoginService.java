package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import requestresult.*;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult loginUser(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDAO.getUser(loginRequest.username());

        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException();
        }

        if (userData == null) {
            throw new UnauthorizedException();
        }

        if (!BCrypt.checkpw(loginRequest.password(), userData.password())) {
            throw new UnauthorizedException();
        }

        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, loginRequest.username()));
        return new LoginResult(loginRequest.username(), authToken);
    }
}
