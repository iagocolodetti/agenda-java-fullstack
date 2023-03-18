package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.model.Error;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

/**
 *
 * @author iagocolodetti
 */
public class UserService {
    
    private final CloseableHttpClient httpclient;
    
    public UserService() {
        httpclient = HttpClients.createDefault();
    }
    
    public Integer create(User user) throws IOException, CustomResponseException {
        HttpPost httpPost = new HttpPost(Api.BASE_URL + "/users");
        httpPost.setHeader("Accept", MediaType.APPLICATION_JSON);
        httpPost.setHeader("Content-type", MediaType.APPLICATION_JSON);
        StringEntity stringEntity = new StringEntity(user.toJson());
        httpPost.setEntity(stringEntity);
        HttpClientResponseHandler<Integer> responseHandler = response -> {
            if (response.getCode() == HttpStatus.SC_CREATED) {
                return HttpStatus.SC_CREATED;
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
}
