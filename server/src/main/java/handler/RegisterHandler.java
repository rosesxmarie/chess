package handler;

import dataaccess.DataAccessException;
import service.RegisterRequest;
import service.RegisterService;
import com.google.gson.Gson;



public class RegisterHandler {

    private final RegisterService registerService;

    public RegisterHandler(RegisterService registerService) {
        this.registerService = registerService;
    }

    public String register(String json) throws DataAccessException {
        var serializer = new Gson();
        var registerRequest = serializer.fromJson(json, RegisterRequest.class);
        var registerResult = registerService.registerUser(registerRequest);
        return serializer.toJson(registerResult);
    }
}
