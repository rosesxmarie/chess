package service;

import requestresult.*;
import dataaccess.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    @Test
    void joinGame() throws DataAccessException {
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        RegisterResult registerResult = registerService.registerUser(registerRequest);
        String authToken = registerResult.authToken();

        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "gameName");
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "WHITE", createGameResult.gameID());
        joinGameService.joinGame(joinGameRequest);

        assertEquals("test", gameDAO.getGame(createGameResult.gameID()).whiteUsername());
    }

    @Test
    void alreadyTaken() throws  DataAccessException {
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        RegisterResult registerResult = registerService.registerUser(registerRequest);
        String authToken = registerResult.authToken();

        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "gameName");
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "WHITE", createGameResult.gameID());
        joinGameService.joinGame(joinGameRequest);

        assertThrows(DataAccessException.class, () -> joinGameService.joinGame(joinGameRequest));
    }

    @Test
    void unauthorizedUser() throws DataAccessException {
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        RegisterResult registerResult = registerService.registerUser(registerRequest);
        String authToken = registerResult.authToken();

        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "gameName");
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeToken", "WHITE", createGameResult.gameID());

        assertThrows(UnauthorizedException.class, () -> joinGameService.joinGame(joinGameRequest));
    }

}