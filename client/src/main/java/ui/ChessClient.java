package ui;

import client.ServerFacade;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import handler.LogoutHandler;
import model.GameData;
import requestresult.*;
import ui.State;
import static ui.EscapeSequences.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import client.ResponseException;



public class ChessClient {
    private final ServerFacade serverFacade;
    private State state = State.LOGGED_OUT;
    private String authToken = null;
    private List<GameData> games = new ArrayList<>();

    public ChessClient(String serverUrl) {
        this.serverFacade = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to CS240 Chess. Type help to get started.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout(params);
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> play(params);
                case "observe" -> observe(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return switch (ex.getStatusCode()) {
                case 400 -> ex.getMessage();
                case 401 -> "You are not authorized for this request.";
                case 403 -> "That is already taken.";
                default -> "Something went wrong. Please try again.";
            };
        }
    }

    public String register(String... params) throws ResponseException {
        if (state == State.LOGGED_IN) {
            throw  new ResponseException(400, "You are already logged in.");
        }
        if (params.length != 3) {
            throw new ResponseException(400,"Expected: register <USERNAME> <PASSWORD> <EMAIL>");
        }

        RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
        RegisterResult registerResult = serverFacade.registerUser(registerRequest);

        authToken = registerResult.authToken();
        state = State.LOGGED_IN;

        return String.format("You are now registered and logged in as %s.", registerResult.username());
    }

    public String login(String... params) throws ResponseException {
        if (state == State.LOGGED_IN) {
            throw  new ResponseException(400, "You are already logged in.");
        }
        if (params.length != 2) {
            throw new ResponseException(400, "Expected: login <USERNAME> <PASSWORD>");
        }

        LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
        LoginResult loginResult = serverFacade.loginUser(loginRequest);

        authToken = loginResult.authToken();
        state = State.LOGGED_IN;

        return String.format("You are logged in as %s.", loginResult.username());
    }

    public String logout(String... params) throws ResponseException {
        if (state == State.LOGGED_OUT) {
            throw new ResponseException(400, "You are not logged in.");
        }
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        serverFacade.logoutUser(logoutRequest);

        authToken = null;
        state = State.LOGGED_OUT;

        return String.format("Logged out successfully.");
    }

    public String create(String... params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException(400, "Expected: create <NAME>");
        }

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, params[0]);
        serverFacade.createGame(createGameRequest);

        return "Game " + params[0] + " created.";
    }

    public String list() throws ResponseException {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResult listGamesResult = serverFacade.listGames(listGamesRequest);
        games = new ArrayList<>(listGamesResult.games());

        if (games.isEmpty()) {
            return "No games found.";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            GameData game = games.get(i);
            result.append(i+1).append(". ").append(" Game Name: ").append(game.gameName())
                    .append(" White: ").append(game.whiteUsername())
                    .append(" Black: ").append(game.blackUsername())
                    .append("\n");
        }
        return result.toString();
    }

    public String play(String... params) throws ResponseException {
        if (params.length != 2) {
            throw new ResponseException(400, "Expected: join <ID> [WHITE|BLACK]");
        }

        int gameNum = 0;
        try {
            gameNum =  Integer.parseInt(params[0]);
        } catch (NumberFormatException ex) {
            throw new ResponseException(400, "Please provide game number.");
        }

        if (gameNum < 0 ||gameNum > games.size()) {
            throw new ResponseException(400, "Invalid game number.");
        }

        String teamColor = params[1].toUpperCase();
        if (!teamColor.equals("WHITE") && !teamColor.equals("BLACK")) {
            throw new ResponseException(400, "Invalid Team Color.");
        }

        GameData game = games.get(gameNum-1);

        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, teamColor, game.gameID());
        serverFacade.joinGame(joinGameRequest);

        return "You joined " + game.gameName() + " as player " + teamColor;
    }

    public String observe(String... params) throws ResponseException {
        if (params.length != 2) {
            throw new ResponseException(400, "Expected: join <ID> [WHITE|BLACK]");
        }

        int gameNum = 0;
        try {
            gameNum =  Integer.parseInt(params[0]);
        } catch (NumberFormatException ex) {
            throw new ResponseException(400, "Please provide game number.");
        }

        if (gameNum < 0 ||gameNum > games.size()) {
            throw new ResponseException(400, "Invalid game number.");
        }

        String teamColor = params[1].toUpperCase();
        if (!teamColor.equals("WHITE") && !teamColor.equals("BLACK")) {
            throw new ResponseException(400, "Invalid Team Color.");
        }

        GameData game = games.get(gameNum-1);

        return "You are observing " + game.gameName();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    - login <USERNAME> <PASSWORD> - to play chess
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }
        return """
                - create <NAME> - a game
                - list - games
                - join <ID> [WHITE|BLACK] - a game
                - logout - when you are done
                - quit - playing chess
                - help - with possible commands
                """;
    }
}
