package com.whatsapp.Whatsappclone.Controllers;


import com.whatsapp.Whatsappclone.Dto.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RealTimeChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void chat(@Payload SendMessageRequest message) {
        log.info("Message received: {}", message);

        String topic = "/topic/chat/" + message.getChatId();
        simpMessagingTemplate.convertAndSend(topic, message);
    }

}
