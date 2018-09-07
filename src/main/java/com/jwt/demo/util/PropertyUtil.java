package com.jwt.demo.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static Properties props;
    static{
        loadProps();
    }

    synchronized static private void loadProps(){
        props = new Properties();
        InputStream inputStream = null;
        try {
                inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties");

                props.load(inputStream);
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            try {
                if(null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}