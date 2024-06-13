package com.whatsapp.Whatsappclone.Security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // Secret key for signing JWTs, injected from application properties
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    // Token expiration time in milliseconds, injected from application properties
    @Value("${jwt.expiration}")
    private Long EXPIRATION;

    /**
     * Generates a JWT for a given user.
     *
     * @param userDetails the user details.
     * @return the generated JWT.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }

    /**
     * Generates a JWT for a given user with extra claims.
     *
     * @param userDetails the user details.
     * @param extraClaims additional claims to include in the JWT.
     * @return the generated JWT.
     */
    public String generateToken(UserDetails userDetails, Map<String, String> extraClaims) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTCreator.Builder jwtBuilder = JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("poojitharirosha")
                .withClaim("role", userDetails.getAuthorities().stream()
                        .filter(a -> a.getAuthority().startsWith("ROLE_"))
                        .findFirst().orElseThrow().getAuthority())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION));
        extraClaims.forEach(jwtBuilder::withClaim);

        return jwtBuilder.sign(algorithm);
    }

    /**
     * Decodes a JWT and verifies its validity.
     *
     * @param token the JWT to decode.
     * @return the decoded JWT.
     * @throws TokenExpiredException if the token has expired.
     */
    public DecodedJWT decodeJWT(String token) throws TokenExpiredException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    /**
     * Extracts a specific claim from a JWT.
     *
     * @param token           the JWT.
     * @param claimsResolver  function to resolve the claim.
     * @param <T>             the type of the claim.
     * @return the claim.
     */
    public <T> T getClaim(String token, Function<Map<String, Claim>, T> claimsResolver) {
        Map<String, Claim> claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Gets the username (subject) from a JWT.
     *
     * @param token the JWT.
     * @return the username.
     */
    public String getUsername(String token) {
        return getClaim(token, claims -> claims.get("sub").asString());
    }

    /**
     * Gets the expiration date from a JWT.
     *
     * @param token the JWT.
     * @return the expiration date.
     */
    public Date getExpiration(String token) {
        return getClaim(token, claims -> claims.get("exp").asDate());
    }

    /**
     * Extracts all claims from a JWT.
     *
     * @param token the JWT.
     * @return a map of all claims.
     */
    public Map<String, Claim> getAllClaims(String token) {
        return decodeJWT(token).getClaims();
    }

    /**
     * Checks if a JWT is valid for a given user.
     *
     * @param token       the JWT.
     * @param userDetails the user details.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            return decodeJWT(token).getSubject().equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (TokenExpiredException ex) {
            return false;
        }
    }

    /**
     * Checks if a JWT has expired.
     *
     * @param token the JWT.
     * @return true if the token has expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        try {
            return decodeJWT(token).getExpiresAt().before(new Date(System.currentTimeMillis()));
        } catch (TokenExpiredException ex) {
            return true;
        }
    }

}
