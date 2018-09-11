package com.jwt.demo.service.impl;

import com.jwt.demo.service.AzureOcrService;
import com.jwt.demo.util.HttpUtils;
import org.apache.http.entity.InputStreamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@Service(value = "azureOcrService")
public class DefaultAzureOcrService implements AzureOcrService {

    private static final Logger logger = Logger.getLogger("DefaultAzureOcrService");

    // Replace the subscriptionKey string value with your valid subscription key.
    public static final String subscriptionKey = "13hc77781f7e4b19b5fcdd72a8df7156";

    // if you want to use the celebrities model, change "landmarks" to "celebrities" here and in
    // uriBuilder.setParameter to use the Celebrities model.
    public static final String uriBase = "https://api.cognitive.azure.cn/vision/v1.0/ocr";

    @Autowired
    private HttpUtils httpUtils;


    @Override
    public String getSerialNumber(String url,InputStream inputStream) {

        HttpClient httpClient = new DefaultHttpClient();
        String serialNumber = null;
        try
        {
            URIBuilder uriBuilder = new URIBuilder(uriBase);

            uriBuilder.setParameter("language", "unk");
            uriBuilder.setParameter("detectOrientation ", "true");

            // Request parameters.
            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.

            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            if (url!=null){

                request.setHeader("Content-Type", "application/json");
                StringEntity requestEntity = new StringEntity("{\"url\":\""+url+"\"}");
                request.setEntity(requestEntity);
            }else {
                request.setHeader("Content-Type", "application/octet-stream");
                InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream);
                request.setEntity(inputStreamEntity);
            }

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                System.out.println("REST Response:\n");
                System.out.println(json.toString(2));
            }
        }
        catch (Exception e)
        {
            // Display error message.
            logger.info(e.getMessage());
        }
        return serialNumber;
    }
}
