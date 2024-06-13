package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Dto.SendMessageRequest;
import com.whatsapp.Whatsappclone.Services.MessageService;
import com.whatsapp.Whatsappclone.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * This controller handles real-time chat functionality.
 * It receives messages from users and forwards them to the appropriate chat topic.
 */
@Controller // Marks this class as a Spring MVC controller.
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required arguments.
@Slf4j // Lombok annotation to generate a logger instance.
public class RealTimeChatController {

    private final SimpMessagingTemplate simpMessagingTemplate; // Template for sending messages to WebSocket clients.
    private final MessageService messageService; // Service for handling message-related operations.
    private final UserService userService; // Service for handling user-related operations.

    /**
     * This method listens for incoming chat messages.
     * When a message is received, it logs the message and forwards it to the appropriate topic.
     *
     * @param message The message payload sent by a user.
     */
    @MessageMapping("/chat") // Maps incoming messages to this method when the destination is "/chat".
    public void chat(@Payload SendMessageRequest message) {
        log.info("Message received: {}", message); // Logs the received message.

        // Constructs the topic string using the chat ID from the message.
        String topic = "/topic/chat/" + message.getChatId();

        // Sends the message to the constructed topic.
        simpMessagingTemplate.convertAndSend(topic, message);
    }
}
