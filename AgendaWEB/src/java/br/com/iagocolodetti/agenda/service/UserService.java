package br.com.iagocolodetti.agenda.service;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.model.Error;
import java.io.IOException;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
        ResponseHandler<Integer> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() == 201) {
                return 201;
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
}
