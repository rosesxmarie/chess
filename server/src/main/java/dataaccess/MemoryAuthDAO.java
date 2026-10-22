package dataaccess;
import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements UserDAO {
    private ArrayList<AuthData> auths = new ArrayList<>();

    @Override
    public void clear() {
        auths.clear();
    }
}
