package br.com.iagocolodetti.agenda.service;

import java.io.IOException;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Error;
import br.com.iagocolodetti.agenda.model.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author iagocolodetti
 */
public class UserService {

    private final OkHttpClient client;

    public UserService() {
        client = new OkHttpClient();
    }

    public int create(User user) throws IOException, CustomResponseException {
        RequestBody body = RequestBody.create(user.toJson(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Api.BASE_URL + "/users")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.code();
            } else {
                Error error = new Error(response.body().string());
                throw new CustomResponseException(error.getMessage(), error.getStatus());
            }
        }
    }
}
