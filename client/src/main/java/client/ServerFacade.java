package client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import client.ResponseException;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) throws ResponseException {
        var request = buildRequest("POST", "/user", registerRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();

    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request !=null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (status != 200) {
            throw new ResponseException(status, "Status " + status + ": " + response.body());
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
