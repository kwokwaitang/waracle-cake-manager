package com.waracle.cake_manager.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    /**
     * Expires in 5 minutes
     */
    public static final long JWT_TOKEN_VALIDITY = TimeUnit.MINUTES.toMillis(5);

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Retrieve the username from JWT token
     *
     * @param token The JWT token
     * @return The username
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieve the expiration date from JWT token
     *
     * @param token The JWT token
     * @return The expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Use of a {@link java.util.function.Function} - takes in one argument and returns a result (potentially a
     * different or of the same type)
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    /**
     * For retrieving any information from token i.e. a claims and a secret key is required
     *
     * @param token The JWT token
     * @return The claims (from the payload) of the JWT token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Check to see if the JWT token has expired
     *
     * @param token The JWT token
     * @return {@code true} when the JWT token has expired, otherwise {@code false}
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    /**
     * Generate a JWT token for the user
     *
     * @param userDetails
     * @return The JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Whilst creating a JWT token...
     * <ol>
     *     <li>Define the claims of the token e.g. the Subject (sub), the Issue At (iat), the Expiration date (exp)
     *     and the identity of the issuer (iss)</li>
     *     <li>Sign the JWT using the HS512 algorithm and with a "secret key"</li>
     *     <li>According to JWS Compact Serialization
     *     (<a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1">JWS Compact Serialization Overview</a>)
     *     compaction of the JWT to a URL-safe string</li>
     * </ol>
     *
     * @param claims  The main payload of the JWT token
     * @param subject
     * @return A URL-safe string of the generated JWT token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)).setIssuer("Kitkat")
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Validate the JWT token by making sure the username matches and that the token itself hasn't expired
     *
     * @param token       The JWT token
     * @param userDetails
     * @return {@code true} when the JWT token is valid, otherwise {@code false}
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
