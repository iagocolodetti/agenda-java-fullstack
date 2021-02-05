package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.model.Error;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 *
 * @author iagocolodetti
 */
public class SessionService {

    private final OkHttpClient client;

    public SessionService() {
        client = new OkHttpClient();
    }

    public String create(User user) throws IOException, CustomResponseException {
        RequestBody body = RequestBody.create(user.toJson(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Api.BASE_URL + "/login")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.header("Authorization");
            } else {
                Error error = new Error(response.body().string());
                throw new CustomResponseException(error.getMessage(), error.getStatus());
            }
        }
    }
}
