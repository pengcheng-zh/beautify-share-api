package com.pacal.share.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pacal.share.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
public class TokenService {
    private static final String PRIVATE_KEY = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAuNRvynIXdOuT6M28" +
            "YwXRaRL/TD+Ux+JJWZ3ZivpSeUHSev/xgfZXD4RGrJjn22simByN5XrfqlFeslpg" +
            "PUr64wIDAQABAkEAtmbPvBZEF9q0qTcABykitHNPB0xn46rpgEtK+OB4q7gf3HnX" +
            "HFIEv7gmhrGAcMFV8wiFK+f76JDD3muxIe1vIQIhAOOa0V4gCauFzEzQVz4VDBz7" +
            "quZFy+Dur9+WDp/MXTRfAiEAz+N798kN/mpl28BDmSQ8ky+tzXpsrB2JutW/MLBk" +
            "Z/0CIE6zKLP4Nf/GGvdwjsobsv2Ok/Bdr/qN7ehM1a+xlYTXAiB0hr+TKahlZkBI" +
            "CCIb8hreKciTN784TqpIBs3PPeBaBQIhAMCP8hHyE9DJTAHVBc1+RWJ+ErK75EGH" +
            "wspbUQOd1ugk";

    public String getToken(String userId, String roleId) {
        return "Bearer " + generateToken( userId, roleId );
    }

    /**
     * 生成 JWT token, userId 写入 audience
     */
    public String generateToken(String userId, String roleId) {
        String sub = UUID.randomUUID().toString();
        String tokenUserId = ( userId == null || userId.isEmpty() ) ? sub : userId;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );
            byte[] encodeByte = Base64.getDecoder().decode( PRIVATE_KEY );
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( encodeByte );
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate( spec );
            Algorithm algorithm = Algorithm.RSA256( null, privateKey );

            return JWT.create()
                    .withIssuer( "beautify-share" )
                    .withExpiresAt( DateUtil.getPlusDayDate( 30 ) )
                    .withSubject( "user" )
                    .withAudience( tokenUserId )
                    .withClaim( "role_id", roleId == null ? "" : roleId )
                    .sign( algorithm );
        } catch ( Exception e ) {
            log.error( "generateToken failed", e );
            return null;
        }
    }
}