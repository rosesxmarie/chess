package server;

import RequestResult.JoinGameResult;
import io.javalin.*;
import dataaccess.*;
import handler.*;
import service.*;


public class Server {
    private final Javalin javalin;
    private final RegisterHandler registerHandler;
    private final ClearApplicationHandler clearApplicationHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final CreateGameHandler createGameHandler;
    private final ListGamesHandler listGamesHandler;
    private final JoinGameHandler joinGameHandler;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        registerHandler = new RegisterHandler(new RegisterService(userDAO, authDAO));
        clearApplicationHandler = new ClearApplicationHandler(new ClearService(userDAO, authDAO,gameDAO));
        loginHandler = new LoginHandler(new LoginService(userDAO, authDAO));
        logoutHandler = new LogoutHandler(new LogoutService(authDAO));
        createGameHandler = new CreateGameHandler(new CreateGameService(authDAO, gameDAO));
        listGamesHandler = new ListGamesHandler(new ListGamesService(authDAO, gameDAO));
        joinGameHandler = new JoinGameHandler(new JoinGameService(authDAO, gameDAO));

        javalin.post("/user", ctx -> ctx.result(registerHandler.register(ctx.body())));
        javalin.delete("/db", ctx -> ctx.result(clearApplicationHandler.clear()));
        javalin.post("/session", ctx -> ctx.result(loginHandler.login(ctx.body())));
        javalin.delete("/session", ctx -> ctx.result(logoutHandler.logoutUser(ctx.header("authorization"))));
        javalin.post("/game", ctx -> ctx.result(createGameHandler.createGame(ctx.header("authorization"), ctx.body())));
        javalin.get("/game", ctx -> ctx.result(listGamesHandler.listGames(ctx.header("authorization"))));
        javalin.put("/game", ctx -> ctx.result(joinGameHandler.joinGame(ctx.header("authorization"), ctx.body())));


        javalin.exception(AlreadyTakenException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.result("{\"message\": \"" + e.getMessage() + "\"}");
        });

        javalin.exception(BadRequestException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result("{\"message\": \"" + e.getMessage() + "\"}");
        });

        javalin.exception(UnauthorizedException.class, (e, ctx) -> {
            ctx.status(401);
            ctx.result("{\"message\": \"" + e.getMessage() + "\"}");
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
