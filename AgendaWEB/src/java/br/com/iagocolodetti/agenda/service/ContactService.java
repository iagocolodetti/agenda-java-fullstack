package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Error;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
        StringEntity stringEntity = new StringEntity(contact.toJson());
        httpPost.setEntity(stringEntity);
        ResponseHandler<Contact> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() == 201) {
                return new Contact(EntityUtils.toString(response.getEntity()));
            } else {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new CustomResponseException("Não foi possível acessar o serviço.", 500);
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
        ResponseHandler<List<Contact>> responseHandler = response -> {
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                return new ObjectMapper().readValue(EntityUtils.toString(entity), new TypeReference<List<Contact>>(){});
            } else {
                if (entity == null) {
                    throw new CustomResponseException("Não foi possível acessar o serviço.", 500);
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
        StringEntity stringEntity = new StringEntity(contact.toJson());
        httpPut.setEntity(stringEntity);
        ResponseHandler<Integer> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                return HttpStatus.SC_NO_CONTENT;
            } else {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new CustomResponseException("Não foi possível acessar o serviço.", 500);
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
        ResponseHandler<Integer> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                return HttpStatus.SC_NO_CONTENT;
            } else {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new CustomResponseException("Não foi possível acessar o serviço.", 500);
                } else {
                    Error error = new Error(EntityUtils.toString(entity));
                    throw new CustomResponseException(error.getMessage(), error.getStatus());
                }
            }
        };
        return httpclient.execute(httpDelete, responseHandler);
    }
}
