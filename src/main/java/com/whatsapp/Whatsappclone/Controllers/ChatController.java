package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Dto.AuthenticationResponse;
import com.whatsapp.Whatsappclone.Dto.ChatRequest;
import com.whatsapp.Whatsappclone.Dto.ChatsIndexDto;
import com.whatsapp.Whatsappclone.Dto.GroupChatRequest;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;
import com.whatsapp.Whatsappclone.Services.ChatService;
import com.whatsapp.Whatsappclone.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/single")
    public ResponseEntity<Chat> createChat(
            @RequestBody ChatRequest request,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChat(requestUser, request.getUserId());

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroup(
            @RequestBody GroupChatRequest request,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChatGroup(request, requestUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatById(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt
    ){
        Chat chat = chatService.findChatById(chatId);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<ChatsIndexDto>> findChatsByUser(
            @RequestParam Integer userId,
            @RequestHeader("Authorization") String jwt
    ){
        List<ChatsIndexDto> chats = chatService.findAllChatsByUserId(userId);

        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToChat(
            @PathVariable Integer chatId,
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile(jwt);
        Chat chat = chatService.addUserToGroup(userId, chatId, requestUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromChat(
            @PathVariable Integer chatId,
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeFromGroup(chatId, userId, requestUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<AuthenticationResponse> deleteChat(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile(jwt);
        chatService.deleteChat(chatId, requestUser.getId());
        AuthenticationResponse response = new AuthenticationResponse("Chat has been deleted successfully.", false);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
