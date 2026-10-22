package dataaccess;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    private ArrayList<AuthData> auths = new ArrayList<>();

    public void clear() {
        auths.clear();
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        auths.add(authData);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData auth : auths) {
            if (Objects.equals(auth.authToken(), authToken)) {
                return auth;
            }
        }
        return null;
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        auths.remove(authData);
    }
}
