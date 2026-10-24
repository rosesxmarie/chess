package server;

import io.javalin.*;
import dataaccess.*;
import handler.*;
import service.*;


public class Server {

    private final Javalin javalin;
    private final RegisterHandler registerHandler;
    private final ClearApplicationHandler clearApplicationHandler;
    private final LoginHandler loginHandler;


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();

        registerHandler = new RegisterHandler(
                new RegisterService(userDAO, authDAO));
        clearApplicationHandler = new ClearApplicationHandler(
                new ClearService(userDAO, authDAO,gameDAO));
        loginHandler = new LoginHandler(
                new LoginService(userDAO, authDAO));


        javalin.post("/user", ctx -> ctx.result(registerHandler.register(ctx.body())));
        javalin.delete("/db", clearApplicationHandler::clear);
        javalin.post("/session", ctx -> ctx.result(loginHandler.register(ctx.body())));

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
