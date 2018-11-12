package com.jwt.demo.facade.impl;

import com.jwt.demo.facade.AzureOcrFacade;
import com.jwt.demo.service.AzureOcrService;
import com.jwt.demo.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.*;
import java.security.cert.X509Certificate;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;

@Service(value = "azureOcrFacade")
public class DefaultAzureOcrFacade implements AzureOcrFacade {

    @Autowired
    private AzureOcrService azureOcrService;

    @Autowired
    private HttpUtils httpUtils;

    @Override
    public String getSerialNumber(String url,InputStream inputStream) {
        String serialNumber = null;
        serialNumber = azureOcrService.getSerialNumber(url, null);

        return null;
    }

    @Override
    public void sendImageToHybris(String path,String url) {

        HttpClient httpClient = new DefaultHttpClient();
        httpClient = wrapClient(httpClient);
        HttpPost request = new HttpPost(url);

//        InputStream inputStream = httpUtils.returnBitMap(path);
        File file = new File("C:\\Users\\jacob.ji\\Desktop\\2.jpg");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("X-AUTH-TOKEN", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJCT1NDSF9DSElOQV9PUEVOSUQiOiIxMjM0IiwiSVNTVUVfVElNRV9NSUxMSVMiOjE1MzYxMTM1ODA4MjQsImlhdCI6MTUzNjExMzU4MCwiZXhwIjoxNTk2MTE5ODgwLCJpc3MiOiJCT1NDSC0zNjAtRlJPTlRFTkQifQ.75jxVNwqH-ykk9QPBR1THZ9On0y-QoQ8JyrqmrM46I0");
            InputStreamEntity inputStreamEntity = new InputStreamEntity(fileInputStream);
            request.setEntity(inputStreamEntity);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // Execute the REST API call and get the response entity.
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.getEntity();
    }

    public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,
                                               String string) {
                }

                public void checkServerTrusted(X509Certificate[] xcs,
                                               String string) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));

            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }



}
