package com.jwt.demo.service;

import java.io.InputStream;

public interface AzureOcrService {

    /*
     * get productSerialNumber
     * @param(image url)
     * */
    String getSerialNumber(String url,InputStream inputStream);
}
