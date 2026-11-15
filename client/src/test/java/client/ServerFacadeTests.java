package client;

import client.ResponseException;
import client.ServerFacade;
import org.junit.jupiter.api.*;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;



public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test2", "test123", "test@byu.edu");
        RegisterResult registerResult = serverFacade.registerUser(registerRequest);

        assertEquals("test2",  registerResult.username());
        assertNotNull(registerResult.authToken());
    }

}
