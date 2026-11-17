package client;

import client.ResponseException;
import client.ServerFacade;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import requestresult.*;
import server.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() throws ResponseException {
        serverFacade.clear();
    }

    @Test
    public void registerUserPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        RegisterResult registerResult = serverFacade.registerUser(registerRequest);

        Assertions.assertEquals("test",  registerResult.username());
        Assertions.assertNotNull(registerResult.authToken());
    }

    @Test
    public void registerUserNegative() throws ResponseException {
        RegisterRequest badRequest = new RegisterRequest(null, "test123", "test@byu.edu");

        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.registerUser(badRequest));
        Assertions.assertEquals(400, ex.getStatusCode());
    }

    @Test
    public void loginUserPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        serverFacade.registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("test", "test123");
        LoginResult loginResult = serverFacade.loginUser(loginRequest);

        Assertions.assertEquals("test",  loginResult.username());
        Assertions.assertNotNull(loginResult.authToken());
    }

    @Test
    public void loginUserNegative() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        serverFacade.registerUser(registerRequest);

        LoginRequest badUsernameRequest = new LoginRequest(null, "test123");

        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.loginUser(badUsernameRequest));
        Assertions.assertEquals(400, ex.getStatusCode());
    }

    @Test
    public void logoutUserPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        serverFacade.registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("test", "test123");
        LoginResult loginResult = serverFacade.loginUser(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        LogoutResult logoutResult = serverFacade.logoutUser(logoutRequest);

        Assertions.assertNotNull(logoutResult);
    }

    @Test
    public void logoutUserNegative() throws ResponseException {
        LogoutRequest logoutRequest = new LogoutRequest("fakeToken");

        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.logoutUser(logoutRequest));
        Assertions.assertEquals(401, ex.getStatusCode());
    }

    @Test
    public void createGamePositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        serverFacade.registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("test", "test123");
        LoginResult loginResult = serverFacade.loginUser(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "gameName");
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);

        Assertions.assertNotNull(createGameResult);
    }

    @Test
    public void createGameNegative() throws ResponseException {
        CreateGameRequest createGameRequest = new CreateGameRequest("fakeToken", "gameName");

        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.createGame(createGameRequest));
        Assertions.assertEquals(401, ex.getStatusCode());
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        serverFacade.registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("test", "test123");
        LoginResult loginResult = serverFacade.loginUser(loginRequest);

        CreateGameRequest createGameRequest1 = new CreateGameRequest(loginResult.authToken(), "gameName1");
        serverFacade.createGame(createGameRequest1);

        CreateGameRequest createGameRequest2 = new CreateGameRequest(loginResult.authToken(), "gameName2");
        serverFacade.createGame(createGameRequest2);

        ListGamesRequest listGamesRequest = new ListGamesRequest(loginResult.authToken());
        ListGamesResult listGamesResult = serverFacade.listGames(listGamesRequest);

        Assertions.assertNotNull(listGamesResult);
    }

    @Test
    public void listGamesNegative() throws ResponseException {
        ListGamesRequest listGamesRequest = new ListGamesRequest("fakeToken");

        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.listGames(listGamesRequest));
        Assertions.assertEquals(401, ex.getStatusCode());
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test123", "test@byu.edu");
        serverFacade.registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("test", "test123");
        LoginResult loginResult = serverFacade.loginUser(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "gameName");
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest(loginResult.authToken(), "WHITE", createGameResult.gameID());
        JoinGameResult joinGameResult = serverFacade.joinGame(joinGameRequest);

        Assertions.assertNotNull(joinGameResult);
    }

    @Test
    public void joinGameNegative() throws ResponseException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("fakeToken", "WHITE", 1);

        ResponseException ex = assertThrows(ResponseException.class, () -> serverFacade.joinGame(joinGameRequest));
        Assertions.assertEquals(401, ex.getStatusCode());
    }
}
