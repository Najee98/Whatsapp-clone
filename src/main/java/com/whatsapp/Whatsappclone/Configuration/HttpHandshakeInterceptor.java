package com.whatsapp.Whatsappclone.Configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;

import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            String authToken = ((ServletServerHttpRequest) request).getServletRequest().getHeader("Authorization");
            if (authToken != null && authToken.startsWith("Bearer ")) {
                String jwtToken = authToken.substring(7);
                // Validate the JWT token here
                boolean isValid = validateToken(jwtToken); // Implement your validation logic
                if (isValid) {
                    return true;
                }
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    private boolean validateToken(String token) {
        // Implement your JWT validation logic here
        try {
            // Assuming you have a JWT utility class
            Claims claims = Jwts.parser()
                    .setSigningKey("yourSecretKey") // Use your secret key
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid JWT token
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // Do nothing
    }
}
