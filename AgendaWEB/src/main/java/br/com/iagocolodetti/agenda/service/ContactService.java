package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Error;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

/**
 *
 * @author iagocolodetti
 */
public class ContactService {

    private final CloseableHttpClient httpclient;

    public ContactService() {
        httpclient = HttpClients.createDefault();
    }

    public Contact create(String authorization, Contact contact) throws IOException, CustomResponseException {
        HttpPost httpPost = new HttpPost(Api.BASE_URL + "/contacts");
        httpPost.setHeader("Accept", MediaType.APPLICATION_JSON);
        httpPost.setHeader("Content-type", MediaType.APPLICATION_JSON);
        httpPost.addHeader("Authorization", authorization);
        StringEntity stringEntity = new StringEntity(contact.toJson(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        HttpClientResponseHandler<Contact> responseHandler = response -> {
            if (response.getCode() == HttpStatus.SC_CREATED) {
                return new Contact(EntityUtils.toString(response.getEntity()));
            } else {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new CustomResponseException("Não foi possível acessar o serviço.", HttpStatus.SC_SERVER_ERROR);
                } else {
                    Error error = new Error(EntityUtils.toString(entity));
                    throw new CustomResponseException(error.getMessage(), error.getStatus());
                }
            }
        };
        return httpclient.execute(httpPost, responseHandler);
    }

    public List<Contact> read(String authorization) throws IOException, CustomResponseException {
        HttpGet httpGet = new HttpGet(Api.BASE_URL + "/contacts");
        httpGet.setHeader("Accept", MediaType.APPLICATION_JSON);
        httpGet.setHeader("Content-type", MediaType.APPLICATION_JSON);
        httpGet.addHeader("Authorization", authorization);
        HttpClientResponseHandler<List<Contact>> responseHandler = response -> {
            HttpEntity entity = response.getEntity();
            if (response.getCode() == HttpStatus.SC_OK) {
                return new ObjectMapper().readValue(EntityUtils.toString(entity), new TypeReference<List<Contact>>(){});
            } else {
                if (entity == null) {
                    throw new CustomResponseException("Não foi possível acessar o serviço.", HttpStatus.SC_SERVER_ERROR);
                } else {
                    Error error = new Error(EntityUtils.toString(entity));
                    throw new CustomResponseException(error.getMessage(), error.getStatus());
                }
            }
        };
        return httpclient.execute(httpGet, responseHandler);
    }

    public Integer update(String authorization, Contact contact) throws IOException, CustomResponseException {
        HttpPut httpPut = new HttpPut(Api.BASE_URL + "/contacts/" + contact.getId());
        httpPut.setHeader("Accept", MediaType.APPLICATION_JSON);
        httpPut.setHeader("Content-type", MediaType.APPLICATION_JSON);
        httpPut.addHeader("Authorization", authorization);
        StringEntity stringEntity = new StringEntity(contact.toJson(), ContentType.APPLICATION_JSON);
        httpPut.setEntity(stringEntity);
        HttpClientResponseHandler<Integer> responseHandler = response -> {
            if (response.getCode() == HttpStatus.SC_NO_CONTENT) {
                return HttpStatus.SC_NO_CONTENT;
            } else {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new CustomResponseException("Não foi possível acessar o serviço.", HttpStatus.SC_SERVER_ERROR);
                } else {
                    Error error = new Error(EntityUtils.toString(entity));
                    throw new CustomResponseException(error.getMessage(), error.getStatus());
                }
            }
        };
        return httpclient.execute(httpPut, responseHandler);
    }

    public Integer destroy(String authorization, int id) throws IOException, CustomResponseException {
        HttpDelete httpDelete = new HttpDelete(Api.BASE_URL + "/contacts/" + id);
        httpDelete.setHeader("Accept", MediaType.APPLICATION_JSON);
        httpDelete.setHeader("Content-type", MediaType.APPLICATION_JSON);
        httpDelete.addHeader("Authorization", authorization);
        HttpClientResponseHandler<Integer> responseHandler = response -> {
            if (response.getCode() == HttpStatus.SC_NO_CONTENT) {
                return HttpStatus.SC_NO_CONTENT;
            } else {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new CustomResponseException("Não foi possível acessar o serviço.", HttpStatus.SC_SERVER_ERROR);
                } else {
                    Error error = new Error(EntityUtils.toString(entity));
                    throw new CustomResponseException(error.getMessage(), error.getStatus());
                }
            }
        };
        return httpclient.execute(httpDelete, responseHandler);
    }
}
