package com.jwt.demo.facade.impl;

import com.jwt.demo.facade.AzureOcrFacade;
import com.jwt.demo.service.AzureOcrService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

@Service(value = "azureOcrFacade")
public class DefaultAzureOcrFacade implements AzureOcrFacade {

    @Autowired
    private AzureOcrService azureOcrService;

    @Override
    public String getSerialNumber(String url,InputStream inputStream) {
        String serialNumber = null;
        serialNumber = azureOcrService.getSerialNumber(url, null);

        return null;
    }

}
