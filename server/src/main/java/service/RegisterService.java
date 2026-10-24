package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import model.AuthData;
import model.UserData;
import java.util.UUID;
import dataaccess.*;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData registerUser(RegisterRequest registerRequest) throws DataAccessException {

        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException();
        }

        if (userDAO.getUser(registerRequest.username()) != null ) {
            throw new AlreadyTakenException();
        }

        userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, registerRequest.username());
        authDAO.createAuth(authData);

        return authData;
    }

}
