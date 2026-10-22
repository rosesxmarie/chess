package dataaccess;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private ArrayList<GameData> games = new ArrayList<>();


    public void clear() {
        games.clear();
    }

    public void createGame(GameData gameData) throws DataAccessException {
        games.add(gameData);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : games) {
            if (Objects.equals(game.gameID(), gameID)) {
                return game;
            }
        }
        return null;
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(games);
    }

    int gameIndex = 0;

    public GameData updateGame(int gameID, GameData newGameData) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                games.set(gameIndex, newGameData);
                return newGameData;
            }
            gameIndex++;
        }
        throw new DataAccessException("Game not found");
    }
}
