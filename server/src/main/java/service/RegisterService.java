package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public class AlreadyTakenException extends Exception {
        public AlreadyTakenException() {
            super("Error: username already taken");
        }

    }

    public AuthData registerUser(UserData userData) throws DataAccessException, AlreadyTakenException {
        if (userDAO.getUser(userData.username()) != null ) {
            throw new AlreadyTakenException();
        }

        userDAO.createUser(userData);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.createAuth(authData);

        return authData;
    }

}
