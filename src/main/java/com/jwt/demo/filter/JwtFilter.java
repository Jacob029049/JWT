package com.jwt.demo.filter;

import com.jwt.demo.bean.HttpResponseBean;
import com.jwt.demo.util.PropertyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by Jacob.Ji on 2018/8/31.
 */
@EnableWebMvc
@Configuration
public class JwtFilter extends GenericFilterBean {

    private Logger logger = Logger.getLogger("JwtFilter");

/*    @Value("${com.jwt.secret}")
    private  String SECRET;

    @Value("${com.jwt.issuer}")
    private  String JWT_ISSUER;*/

    private static final String TOKEN_HEADER = "X-AUTH-TOKEN";
    private static final String HTTP_STATUS_ERROR = "ERROR";
    private static final String HTTP_STATUS_OK = "OK";
    private static final String HTTP_TOKEN_MESSAGE_INVALID = "Token validate failure. ";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //secretKey
        String SECRET = PropertyUtil.getProperty("com.jwt.secret","12345678");
        String JWT_ISSUER = PropertyUtil.getProperty("com.jwt.issuer","87654321");

        final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Base64.getEncoder().encodeToString(SECRET.getBytes()));

        //请求头上的token
        String jsonTokenWeb = request.getHeader(TOKEN_HEADER);

        HttpResponseBean responseWsBean = new HttpResponseBean();

        //exp 、iss、sign、 validate
        try {
            Claims claims = Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(jsonTokenWeb)
                    .getBody();
            String iss = Objects.isNull(claims.get("iss"))?null:String.valueOf(claims.get("iss"));
            if (!JWT_ISSUER.equals(iss)){
                setResponseWsBean(responseWsBean, HTTP_STATUS_ERROR, HTTP_TOKEN_MESSAGE_INVALID + "issuer is incorrect. ");
                response.getWriter().write(JSONObject.fromObject(responseWsBean).toString());
            }else{
                //validate pass
                filterChain.doFilter(request,response);
            }
        }catch (Exception e){
            logger.info("parse jwttoken error :"+ e.getMessage());
            setResponseWsBean(responseWsBean, HTTP_STATUS_ERROR, HTTP_TOKEN_MESSAGE_INVALID + e.getMessage());
            response.getWriter().write(JSONObject.fromObject(responseWsBean).toString());
        }

    }

    private void setResponseWsBean(HttpResponseBean responseWsBean, String httpStatus , String message){
        responseWsBean.setHttpStatus(httpStatus);
        responseWsBean.setMessage(message);
    }

}
