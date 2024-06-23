package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Dto.*;
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

    /**
     * Endpoint to create a single user chat.
     *
     * @param request the request body containing userId of the target user
     * @param jwt     JWT token in the authorization header
     * @return ResponseEntity with created Chat object
     */
    @PostMapping("/single")
    public ResponseEntity<Chat> createChat(
            @RequestBody ChatRequest request,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile();
        Chat chat = chatService.createChat(requestUser, request.getUserId());

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    /**
     * Endpoint to create a group chat.
     *
     * @param request the request body containing chat details
     * @param jwt     JWT token in the authorization header
     * @return ResponseEntity with created Chat object
     */
    @PostMapping("/group")
    public ResponseEntity<Chat> createGroup(
            @RequestBody GroupChatRequest request,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile();
        Chat chat = chatService.createChatGroup(request, requestUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    /**
     * Endpoint to find a chat by its ID.
     *
     * @param chatId the ID of the chat to find
     * @param jwt    JWT token in the authorization header
     * @return ResponseEntity with found Chat object
     */
    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatById(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt
    ){
        Chat chat = chatService.findChatById(chatId);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    /**
     * Endpoint to get details of a chat by its ID.
     *
     * @param chatId the ID of the chat to get details for
     * @param jwt    JWT token in the authorization header
     * @return ResponseEntity with ChatDetailsDto object
     */
    @GetMapping("/details/{chatId}")
    public ResponseEntity<ChatDetailsDto> chatDetails(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt
    ){
        ChatDetailsDto chatDetails = chatService.getChatDetails(chatId);

        return new ResponseEntity<>(chatDetails, HttpStatus.OK);
    }

    /**
     * Endpoint to find all chats of the current user.
     *
     * @param jwt JWT token in the authorization header
     * @return ResponseEntity with a list of ChatsIndexDto objects
     */
    @GetMapping("/by-user")
    public ResponseEntity<List<ChatsIndexDto>> findChatsByUser(
            @RequestHeader("Authorization") String jwt
    ){
        AppUser user = userService.findUserProfile();
        List<ChatsIndexDto> chats = chatService.findAllChatsByUserId(user.getId());

        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    /**
     * Endpoint to add a user to a group chat.
     *
     * @param chatId  the ID of the chat to add the user to
     * @param userId  the ID of the user to add to the chat
     * @param jwt     JWT token in the authorization header
     * @return ResponseEntity with updated Chat object
     */
    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToChat(
            @PathVariable Integer chatId,
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile();
        Chat chat = chatService.addUserToGroup(userId, chatId, requestUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    /**
     * Endpoint to remove a user from a group chat.
     *
     * @param chatId  the ID of the chat to remove the user from
     * @param userId  the ID of the user to remove from the chat
     * @param jwt     JWT token in the authorization header
     * @return ResponseEntity with updated Chat object
     */
    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromChat(
            @PathVariable Integer chatId,
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile();
        Chat chat = chatService.removeFromGroup(chatId, userId, requestUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    /**
     * Endpoint to delete a chat.
     *
     * @param chatId the ID of the chat to delete
     * @param jwt    JWT token in the authorization header
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<AuthenticationResponse> deleteChat(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt
    ){
        AppUser requestUser = userService.findUserProfile();
        chatService.deleteChat(chatId, requestUser.getId());
        AuthenticationResponse response = new AuthenticationResponse("Chat has been deleted successfully.", false);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/group/update")
    public ResponseEntity<Chat> updateGroup(
            @RequestParam Integer chatId,
            @RequestBody UpdateGroupRequest request
    ){
        return new ResponseEntity<>(chatService.updateGroup(chatId, request), HttpStatus.OK);
    }

}
