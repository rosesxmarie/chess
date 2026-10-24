package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    @Test
    void createGame() throws DataAccessException {

        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        AuthData registerResult = registerService.registerUser(registerRequest);
        String authToken = registerResult.authToken();

        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "gameName");
        GameData createGameResult = createGameService.createGame(createGameRequest);

        assertNotNull(gameDAO.getGame(createGameResult.gameID()));


    }

    @Test
    void badRequest() throws DataAccessException{
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameRequest BadRequest = new CreateGameRequest(null, "gameName");

        assertThrows(DataAccessException.class, () -> createGameService.createGame(BadRequest));
    }

    @Test
    void unauthorized() throws DataAccessException{
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameRequest unauthorizedRequest = new CreateGameRequest("fakeToken", "gameName");


        assertThrows(UnauthorizedException.class, () -> createGameService.createGame(unauthorizedRequest));
    }

}