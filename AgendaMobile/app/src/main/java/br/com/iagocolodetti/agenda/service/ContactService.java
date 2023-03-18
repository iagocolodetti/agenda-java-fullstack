package br.com.iagocolodetti.agenda.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Error;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author iagocolodetti
 */
public class ContactService {

    private final OkHttpClient client;

    public ContactService() {
        client = new OkHttpClient();
    }

    public Contact create(String authorization, Contact contact) throws IOException, CustomResponseException {
        RequestBody body = RequestBody.create(contact.toJson(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Api.BASE_URL + "/contacts")
                .addHeader("Authorization", authorization)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return new Contact(response.body().string());
            } else {
                Error error = new Error(response.body().string());
                throw new CustomResponseException(error.getMessage(), error.getStatus());
            }
        }
    }

    public List<Contact> read(String authorization) throws IOException, CustomResponseException {
        Request request = new Request.Builder()
                .url(Api.BASE_URL + "/contacts")
                .addHeader("Authorization", authorization)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return new ObjectMapper().readValue(response.body().string(), new TypeReference<List<Contact>>(){});
            } else {
                Error error = new Error(response.body().string());
                throw new CustomResponseException(error.getMessage(), error.getStatus());
            }
        }
    }

    public int update(String authorization, Contact contact) throws IOException, CustomResponseException {
        RequestBody body = RequestBody.create(contact.toJson(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Api.BASE_URL + "/contacts/" + contact.getId())
                .addHeader("Authorization", authorization)
                .put(body)
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

    public int destroy(String authorization, int id) throws IOException, CustomResponseException {
        Request request = new Request.Builder()
                .url(Api.BASE_URL + "/contacts/" + id)
                .addHeader("Authorization", authorization)
                .delete()
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
