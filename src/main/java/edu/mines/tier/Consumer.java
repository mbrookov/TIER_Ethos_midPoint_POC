package edu.mines.tier;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/*
  Consume records from Ethos
 */
public class Consumer {
    String URL = "https://integrate.elluciancloud.com/consume";

    /* See https://resources.elluciancloud.com/bundle/ethos_integration_ref_apis/page/mqs_api.html */

    public String consume(KeyMaster k) {
        String message=null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL);
        System.out.println("Calling addheader with " + k.getBearer());
        //httpGet.addHeader("Authorization: Bearer", k.getBearer());
        httpGet.addHeader("Authorization", "Bearer " + k.getBearer());
        try {
            /* Look over headers */
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine());
            System.out.println(response.getAllHeaders().length);
            Header[] h = response.getAllHeaders();
            for (Header s : h) {
                System.out.println(s);
            }

            /* get the message body */
            HttpEntity entity = response.getEntity();
            if (entity!=null)
            {
                InputStream in = entity.getContent();
                StringWriter w = new StringWriter();
                String encoding = StandardCharsets.UTF_8.name();
                org.apache.commons.io.IOUtils.copy(in,w,encoding);
                message=w.toString();
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }
}
