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

import java.text.SimpleDateFormat;

@Controller
@RequiredArgsConstructor
public class RealTimeChatController {

    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("/send")
    @SendTo("/messages")
    public Message send(SendMessageRequest message) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        AppUser user = userService.findUserByUsername(username);

        return messageService.sendMessage(message, user);
    }

}