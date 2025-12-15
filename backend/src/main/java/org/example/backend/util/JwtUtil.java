package org.example.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.backend.domain.AbstractUser;
import org.example.backend.service.AbstractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final Long EXPIRATION = 604800000L; // 7 days(ms)
    /** Use a fixed server-side secret instead of user password */
    private static final String SECRET = "nyu-activity-center-jwt-secret-please-change";
    private static AbstractUserService abstractUserService;

    @Autowired
    public void setUserService(AbstractUserService abstractUserService) {
        JwtUtil.abstractUserService = abstractUserService;
    }

    /** Create token with userId and fixed secret */
    public static String getToken(String userId) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION);
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        return JWT.create()
                .withHeader(header)
                .withClaim("userId", userId)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET));
    }

    /** Verify token and load user by id */
    public static AbstractUser verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt.getExpiresAt() != null && jwt.getExpiresAt().before(new Date())) {
                return null;
            }
            long userId = Long.parseLong(jwt.getClaim("userId").asString());
            return abstractUserService.findUserById(userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static long getIdByToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return Long.parseLong(jwt.getClaim("userId").asString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}
