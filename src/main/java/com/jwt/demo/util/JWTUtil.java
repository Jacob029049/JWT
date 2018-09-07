package com.jwt.demo.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//jwt含有三部分：头部（header）、载荷（payload）、签证（signature）
/*
*(1)头部一般有两部分信息：声明类型、声明加密的算法（通常使用HMAC SHA256）
*(2)载荷该部分一般存放一些有效的信息。jwt的标准定义包含五个字段：
*    -iss：该JWT的签发者
    - sub: 该JWT所面向的用户
    - aud: 接收该JWT的一方
    - exp(expires): 什么时候过期，这里是一个Unix时间戳
    - iat(issued at): 在什么时候签发的

* (3)签证（signature） JWT最后一个部分。该部分是使用了HS256加密后的数据；包含三个部分：
* */

@Service("jwtUtil")
public class JWTUtil {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Value("${com.jwt.secret}")
    private  String SECRET;

    @Value("${com.jwt.issuer}")
    private  String JWT_ISSUER;

    public String creatJwtToken(){
        final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        final long nowMillis = System.currentTimeMillis();
        //过期时间5分钟
        final long ttlMillis = 5 * 60 * 100000;
        final long expMillis = nowMillis + ttlMillis;

        final Date now = new Date(nowMillis);
        final Date exp = new Date(expMillis);

        //Create the Signature SecretKey
        final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Base64.getEncoder().encodeToString(SECRET.getBytes()));
        final Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        final Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");

        //add JWT Parameters
        final JwtBuilder builder = Jwts.builder()
                                        .setHeaderParams(headerMap)
                                        .setIssuedAt(now)
                                        .setExpiration(exp)
                                        .setIssuer(JWT_ISSUER)
                                        .signWith(signatureAlgorithm, signingKey);

        logger.info("JWT[" + builder.compact()+ "]");
        return builder.compact();

    }

    public Claims parseJWTToken(String token)
    {
        Claims claims = null;
        try
        {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET)).parseClaimsJws(token)
                    .getBody();
        }
        catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
                | IllegalArgumentException e)
        {
            logger.info("Parse JWT errror " + e.getMessage());
            return null;
        }
        return claims;
    }
}