package dataaccess;
import model.GameData;
import java.util.ArrayList;

public class MemoryGameDAO implements UserDAO {
    private ArrayList<GameData> games = new ArrayList<>();

    @Override
    public void clear() {
        games.clear();
    }
}
