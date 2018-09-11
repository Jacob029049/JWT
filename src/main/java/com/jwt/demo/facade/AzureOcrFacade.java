package com.jwt.demo.facade;

import java.io.InputStream;

public interface AzureOcrFacade {

    /*
    * get productSerialNumber
    * @param(image url)
    * */
  String getSerialNumber(String url,InputStream inputStream);
}
