package service;

import requestresult.*;
import dataaccess.*;
import org.junit.jupiter.api.Test;

class ListGamesServiceTest {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    @Test
    void listGames() throws DataAccessException {
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        RegisterResult registerResult = registerService.registerUser(registerRequest);
        String authToken = registerResult.authToken();

        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "gameName");
        createGameService.createGame(createGameRequest);

        CreateGameRequest createGameRequest1 = new CreateGameRequest(authToken, "gameName2");
        createGameService.createGame(createGameRequest1);

        CreateGameRequest createGameRequest2 = new CreateGameRequest(authToken, "gameName3");
        createGameService.createGame(createGameRequest2);

        ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO);
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        listGamesService.listGames(listGamesRequest);
    }
}