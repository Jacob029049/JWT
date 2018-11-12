package com.jwt.demo.controller;

import com.jwt.demo.facade.AzureOcrFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class AzureOcrController {

    @Autowired
    private AzureOcrFacade azureOcrFacade;

    @RequestMapping(value = "/ocr/getSerialNumber",method = RequestMethod.GET)
    @ResponseBody
    public String getSerialNumber(@RequestParam String url,HttpServletRequest request){
        if (Strings.isNotBlank(url)){
            String serialNumber = azureOcrFacade.getSerialNumber(url,null);
        }else {
            try {
                InputStream inputStream = request.getInputStream();
                String serialNumber = azureOcrFacade.getSerialNumber(null,inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return "Welcome to home page.";
    }

    @RequestMapping(value = "ocr/transfer", method = RequestMethod.GET)
    public void transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = null;
       azureOcrFacade.sendImageToHybris(path,
               "https://localhost:9002/boschproductregistrationwebservices/v2/boschstore-ro/registrations/products/images");
    }
}
