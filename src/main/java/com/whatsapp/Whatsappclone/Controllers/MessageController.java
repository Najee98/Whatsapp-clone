package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Dto.AuthenticationResponse;
import com.whatsapp.Whatsappclone.Dto.ChatMessagesDto;
import com.whatsapp.Whatsappclone.Dto.SendMessageRequest;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Message;
import com.whatsapp.Whatsappclone.Services.MessageService;
import com.whatsapp.Whatsappclone.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    /**
     * Endpoint to send a message to a chat.
     *
     * @param request the request body containing message details
     * @param jwt     JWT token in the authorization header
     * @return ResponseEntity with sent Message object
     */
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestBody SendMessageRequest request,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser fromUser = userService.findUserProfile();
        Message message = messageService.sendMessage(request, fromUser);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve messages of a chat by chatId.
     *
     * @param chatId the ID of the chat to retrieve messages for
     * @param jwt    JWT token in the authorization header
     * @return ResponseEntity with a list of ChatMessagesDto objects
     */
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<ChatMessagesDto>> findChatMessages(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser user = userService.findUserProfile();
        List<ChatMessagesDto> messages = messageService.getChatMessages(chatId, user);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     * Endpoint to delete a message by messageId.
     *
     * @param messageId the ID of the message to delete
     * @param jwt       JWT token in the authorization header
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<AuthenticationResponse> deleteMessage(
            @PathVariable Integer messageId,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser user = userService.findUserProfile();
        messageService.deleteMessage(messageId, user);

        AuthenticationResponse response = new AuthenticationResponse("Message deleted successfully.", false);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
