package com.jwt.demo.service.impl;

import com.jwt.demo.bean.AzureOcrBean;
import com.jwt.demo.service.AzureOcrService;
import com.jwt.demo.util.HttpUtils;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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
        try
        {
            URIBuilder uriBuilder = new URIBuilder(uriBase);

            uriBuilder.setParameter("language", "zh-Hans");
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

                logger.info("img json from azureOCR : "+ jsonString);

                JSONObject jsonObject = JSONObject.fromObject(jsonString);

                Map classMap = new HashMap();
                classMap.put("regions",AzureOcrBean.class);
                classMap.put("lines",AzureOcrBean.class);
                classMap.put("words",AzureOcrBean.class);

                AzureOcrBean azureOcrBean = (AzureOcrBean) JSONObject.toBean(jsonObject, AzureOcrBean.class,classMap);

                StringBuilder bareToolNumber = new StringBuilder();
                StringBuilder serialNumber = new StringBuilder();

                for (AzureOcrBean region :azureOcrBean.getRegions()){
                    for (AzureOcrBean line : region.getLines()){

                        List<AzureOcrBean> wordList = line.getWords();
                        int size = wordList.size();
                        for (int i =0;i < size;i++){
                            AzureOcrBean word = line.getWords().get(i);
                            String text = word.getText();
                            //判断当前text是否是baretoolNumber开头
                            if (isBareToolNumber(text)){
                                bareToolNumber.append(text);
                                //拼接baretoolNumber
                                if (10>bareToolNumber.toString().length()&&(i+1)<size){
                                    bareToolNumber.append(line.getWords().get(i+1).getText());
                                }
                                if (10>bareToolNumber.toString().length()&&(i+2)<size){
                                    bareToolNumber.append(line.getWords().get(i+2).getText());
                                }
                                System.out.print("PTbareToolNumber: "+bareToolNumber.toString()+"\n");
                            }
                            //判断纯数字组合为serialNumber
                            if (isSerialNumber(text)){
                                serialNumber.append(text);
                                //TT product
                                if (bareToolNumber.toString().length()==0){
                                    for (int j=0;j<i;j++){
                                        String wordTTBareToolNumber = wordList.get(j).getText();
                                        if (isNumeric(wordTTBareToolNumber)){
                                            bareToolNumber.append(wordTTBareToolNumber);
                                        }
                                    }
                                }
                                System.out.print("TTbareToolNumber: "+bareToolNumber.toString()+"\n");
                                System.out.print("serialNumber: "+serialNumber.toString()+"\n");
                            }
                        }
                    }
                }

//                System.out.println("REST Response:\n");
//                System.out.println(json.toString(2));
            }
        }
        catch (Exception e)
        {
            // Display error message.
            logger.info(e.getMessage());
        }
        return null;
    }

    private boolean isBareToolNumber (String text){
        boolean isBareToolNumber =false;

        isBareToolNumber = text.startsWith("3601")||text.startsWith("3600")||text.startsWith("0602")||text.startsWith("3603")||text.startsWith("0600");

        return isBareToolNumber;
    }

    private boolean isSerialNumber (String text){
        boolean isSerialNumber =false;

        isSerialNumber = text.length()>8 && isNumeric(text);

        return isSerialNumber;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
