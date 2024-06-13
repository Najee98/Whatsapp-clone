package com.whatsapp.Whatsappclone.Configuration;

import com.whatsapp.Whatsappclone.Security.JWT.JwtTokenUtil;
import com.whatsapp.Whatsappclone.Services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker.
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // Sets the order of this configuration class
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required arguments.
@Slf4j // Lombok annotation to generate a logger instance.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenUtil jwtTokenUtil; // Utility class for JWT token operations.
    private final UserDetailsServiceImpl userDetailsService; // Service to load user details.

    /**
     * Registers WebSocket endpoints that the clients will use to connect to the WebSocket server.
     *
     * @param registry the StompEndpointRegistry used to register the endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS(); // Register an endpoint with SockJS fallback.
        registry.addEndpoint("/ws"); // Register a standard WebSocket endpoint.
    }

    /**
     * Configures the message broker options.
     *
     * @param registry the MessageBrokerRegistry used to configure the message broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // Enable a simple in-memory message broker for topic destinations.
        registry.setApplicationDestinationPrefixes("/app"); // Set application destination prefixes for message mapping.
    }

    /**
     * Configures the inbound channel used to receive messages from WebSocket clients.
     * Adds an interceptor to handle authentication.
     *
     * @param registration the ChannelRegistration used to configure the inbound channel.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                log.info("Headers: {}", accessor); // Log the headers of the message.

                assert accessor != null;
                if (StompCommand.CONNECT.equals(accessor.getCommand())) { // Check if the command is CONNECT.
                    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                    assert authorizationHeader != null;
                    String token = authorizationHeader.substring(7); // Extract the token from the Authorization header.

                    String username = jwtTokenUtil.getUsername(token); // Get the username from the token.
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Load the user details.
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); // Set authentication in the security context.

                    accessor.setUser(usernamePasswordAuthenticationToken); // Set the user in the accessor.
                }

                return message; // Return the message.
            }
        });
    }
}
