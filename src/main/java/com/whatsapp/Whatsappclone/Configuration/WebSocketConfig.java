package com.whatsapp.Whatsappclone.Configuration;

import com.whatsapp.Whatsappclone.Services.MessageService;
import com.whatsapp.Whatsappclone.Services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    MessageService messageService;
    UserService userService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageHandler(), "/message")
                .setAllowedOrigins("*");
    }

    @Bean
    WebSocketHandler messageHandler() {
        return new MessageHandler(messageService, userService);
    }


//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .setAllowedOrigins("*")  // Adjust as needed
//                .addInterceptors(new HttpHandshakeInterceptor())  // Add the interceptor
//                .withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/messages");
//        config.setApplicationDestinationPrefixes("/app");
//    }
}

