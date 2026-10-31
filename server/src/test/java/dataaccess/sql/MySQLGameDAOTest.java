package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MySQLGameDAOTest {
    private GameDAO gameDAO;

    @BeforeEach
    void setUp() {
        try {
            gameDAO = new MySQLGameDAO();
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clear() throws DataAccessException {
        gameDAO.createGame(new GameData(1, "whiteUsername", "blackUsername",
                "gameName", new ChessGame()));
        gameDAO.clear();
        assertNull(gameDAO.getGame(1));
    }

    @Test
    void createGameValid() throws DataAccessException{
        assertDoesNotThrow(() -> gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName", new ChessGame())));
    }

    @Test
    void createGameInvalid() throws DataAccessException {
        gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName", new ChessGame()));
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName", new ChessGame())));
    }

    @Test
    void getGameValid() throws DataAccessException {
        gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName", new ChessGame()));
        GameData game = gameDAO.getGame(1);
        assertNotNull(game);
        assertEquals("gameName", game.gameName());
    }

    @Test
    void getGameInvalid() throws DataAccessException {
        gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName", new ChessGame()));
        assertNull(gameDAO.getGame(2));
    }

    @Test
    void listGamesValid()  throws DataAccessException {
        gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName1", new ChessGame()));
        gameDAO.createGame(new GameData(2, "whiteUsername",
                "blackUsername", "gameName2", new ChessGame()));

        ArrayList<GameData> listGames = gameDAO.listGames();

        assertNotNull(listGames);
    }

    @Test
    void listGamesEmpty()  throws DataAccessException {
        gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName1", new ChessGame()));
        gameDAO.createGame(new GameData(2, "whiteUsername",
                "blackUsername", "gameName2", new ChessGame()));

        gameDAO.clear();
        ArrayList<GameData> listGames = gameDAO.listGames();

        assertTrue(listGames.isEmpty());
    }

    @Test
    void updateGameValid() throws DataAccessException {
        gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName1", new ChessGame()));

        gameDAO.updateGame(1, new GameData(1, "updateWhiteUsername",
                "blackUsername", "gameName1", new ChessGame()));

        GameData game = gameDAO.getGame(1);
        assertNotNull(game);
        assertEquals("updateWhiteUsername", game.whiteUsername());
    }

    @Test
    void updateGameInvalid() throws DataAccessException {
        gameDAO.createGame(new GameData(1, "whiteUsername",
                "blackUsername", "gameName1", new ChessGame()));

        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(1, new GameData(1, "whiteUsername",
                "blackUsername", null, new ChessGame())));
    }
}