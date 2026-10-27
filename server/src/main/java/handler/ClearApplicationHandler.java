package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;


public class ClearApplicationHandler {

    private final ClearService clearService;

    public ClearApplicationHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public String clear() throws DataAccessException {
        var serializer = new Gson();
        var clearResult = clearService.clearApplication();
        return serializer.toJson(clearResult);
    }
}
