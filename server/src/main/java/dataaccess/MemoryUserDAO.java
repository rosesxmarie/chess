package dataaccess;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    private ArrayList<UserData> users = new ArrayList<>();

    public void clear() {

        users.clear();
    }

    public void createUser(UserData userData) throws DataAccessException {
        users.add(userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : users) {
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null;
    }
}
