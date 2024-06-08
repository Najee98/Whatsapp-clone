import com.whatsapp.Whatsappclone.Models.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RealTimeChat {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/group/public")
    public Message receiveMessage(@Payload Message message) {
        try {
            // Process the received message

            // Broadcast the message to all clients in the same group
            simpMessagingTemplate.convertAndSend("/group/" + message.getChat().getId().toString(), message);

            return message;
        } catch (Exception e) {
            // Handle exceptions
            // You can log the exception or send a custom error message to the client
            e.printStackTrace();
            return null; // Or return an error message object
        }
    }
}