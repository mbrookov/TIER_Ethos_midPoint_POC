package edu.mines.tier;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class KeyMaster {
    private String apiKey = "00000000-0000-0000-0000-000000000000";
    private String URL = "https://integrate.elluciancloud.com/auth";
    private String bearer;
    private Instant timeKeyAcquierd = null;

    public KeyMaster() {
        getKey();
    }

    public KeyMaster(String key) {
        apiKey=key;
        getKey();
    }

    public String getBearer() {
        /* Need to get a new key every 5 minutes */
        if ((timeKeyAcquierd.getEpochSecond() + 20) > Instant.now().getEpochSecond()) {
            return bearer;
        }
        else {
            getKey();
            return bearer;
        }
    }

    private void getKey() {

        /* no data, just an empty message body
           Ellucian wants a post request, but the api Key is in the header
         */

        StringEntity emptyEntity = new StringEntity("",
                ContentType.create("text/plain", "UTF-8"));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL);
        System.out.println("Key: " + apiKey);
        httpPost.addHeader("Authorization: Bearer",apiKey);
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream in = entity.getContent();
                StringWriter w = new StringWriter();
                String encoding = StandardCharsets.UTF_8.name();
                org.apache.commons.io.IOUtils.copy(in, w, encoding);
                bearer = w.toString();
                timeKeyAcquierd = Instant.now();
                in.close();
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
