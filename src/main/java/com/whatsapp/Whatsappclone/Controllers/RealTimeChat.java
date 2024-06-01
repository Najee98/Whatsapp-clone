package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Models.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor
public class RealTimeChat {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/goup/public")
    public Message receiveMessage(@Payload Message message){

        simpMessagingTemplate.convertAndSend("/goup/" + message.getChat().getId().toString(), message);

        return message;
    }
}
