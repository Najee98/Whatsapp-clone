package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Dto.SendMessageRequest;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Message;
import com.whatsapp.Whatsappclone.Services.MessageService;
import com.whatsapp.Whatsappclone.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RealTimeChatController {

    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(@Payload SendMessageRequest message) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        AppUser user = userService.findUserByUsername(username);

        return messageService.sendMessage(message, user);
    }

//    private final SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/message")
//    @SendTo("/group/public")
//    public Message receiveMessage(@Payload Message message) {
//        try {
//            // Process the received message
//
//            // Broadcast the message to all clients in the same group
//            simpMessagingTemplate.convertAndSend("/group/" + message.getChat().getId().toString(), message);
//
//            return message;
//        } catch (Exception e) {
//            // Handle exceptions
//            // You can log the exception or send a custom error message to the client
//            e.printStackTrace();
//            return null; // Or return an error message object
//        }
//    }
}