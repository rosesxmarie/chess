package dataaccess;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private ArrayList<UserData> users = new ArrayList<>();

    @Override
    public void clear() {
        users.clear();
    }
}
