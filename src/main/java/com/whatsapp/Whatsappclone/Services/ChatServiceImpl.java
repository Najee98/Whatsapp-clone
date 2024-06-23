package com.whatsapp.Whatsappclone.Services;

import com.whatsapp.Whatsappclone.Dto.ChatDetailsDto;
import com.whatsapp.Whatsappclone.Dto.ChatsIndexDto;
import com.whatsapp.Whatsappclone.Dto.GroupChatRequest;
import com.whatsapp.Whatsappclone.Dto.UpdateGroupRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.ChatException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;
import com.whatsapp.Whatsappclone.Repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageService messageService;

    /**
     * Creates a chat between the requesting user and the target user.
     * If a chat already exists between them, it is returned instead.
     *
     * @param requestUser the user initiating the chat
     * @param targetUserId the ID of the target user
     * @return the created or existing chat
     * @throws UserException if the target user cannot be found
     */
    @Override
    public Chat createChat(AppUser requestUser, Integer targetUserId) throws UserException {
        AppUser targetUser = userService.findUserById(targetUserId);

        Chat existingChat = chatRepository.findChatByUsers(requestUser, targetUser);
        if (existingChat != null) {
            return existingChat;
        }

        Chat chat = new Chat();
        chat.setName("default chat name"); // Default chat name; could be changed to target user's name
        chat.setCreatedBy(requestUser);
        chat.getUsers().add(requestUser);
        chat.getUsers().add(targetUser);
        chat.setGroup(false);
        chat.setCreatedAt(LocalDateTime.now());

        return chatRepository.save(chat);
    }

    /**
     * Finds a chat by its ID.
     *
     * @param chatId the ID of the chat to find
     * @return the found chat
     * @throws ChatException if the chat cannot be found
     */
    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat with id: " + chatId + " not found"));
    }

    /**
     * Retrieves all chats for a user, including single and group chats.
     *
     * @param userId the ID of the user whose chats are to be retrieved
     * @return a list of chat index DTOs
     */
    public List<ChatsIndexDto> findAllChatsByUserId(Integer userId) {
        AppUser user = userService.findUserById(userId);
        List<Object[]> chatDataList = chatRepository.findChatDataByUserId(user);
        List<ChatsIndexDto> chats = new ArrayList<>();

        for (Object[] chatData : chatDataList) {
            Integer chatId = (Integer) chatData[0];
            boolean isGroup = (boolean) chatData[3];
            String chatName = isGroup ? (String) chatData[1] : getChatSecondUser(chatId).getFullName();
            String chatImage = isGroup ? (String) chatData[2] : getChatSecondUser(chatId).getProfilePicture();

            String lastMessageContent = messageService.getLastMessageContentForChat(chatId, user);
            LocalDateTime lastMessageTimeStamp = messageService.getLastMessageTimeStampForChat(chatId, user);
            LocalDateTime createdAt = (LocalDateTime) chatData[4];

            // Include group chats and only single chats with messages
            if (isGroup || lastMessageTimeStamp != null) {
                ChatsIndexDto dto = new ChatsIndexDto(chatId, chatName, chatImage, isGroup, lastMessageContent, lastMessageTimeStamp, createdAt);
                chats.add(dto);
            }
        }

        // Sort chats list based on last message timestamp for single chats,
        // and creation date for group chats if lastMessageTimeStamp is null
        chats.sort((chat1, chat2) -> {
            LocalDateTime time1 = chat1.getLastMessageTimeStamp() != null ? chat1.getLastMessageTimeStamp() : chat1.getCreatedAt();
            LocalDateTime time2 = chat2.getLastMessageTimeStamp() != null ? chat2.getLastMessageTimeStamp() : chat2.getCreatedAt();
            return time2.compareTo(time1); // Sort in descending order
        });

        return chats;
    }

    /**
     * Creates a group chat.
     *
     * @param request the request containing group chat details
     * @param requestUser the user initiating the group chat
     * @return the created group chat
     * @throws UserException if a user cannot be found
     */
    @Override
    public Chat createChatGroup(GroupChatRequest request, AppUser requestUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setImage(request.getChatImage());
        group.setName(request.getChatName());
        group.setCreatedBy(requestUser);
        group.getAdmins().add(requestUser);
        group.getUsers().add(requestUser);
        group.setCreatedAt(LocalDateTime.now());

        for (Integer userId : request.getUserIds()) {
            AppUser groupUser = userService.findUserById(userId);
            group.getUsers().add(groupUser);
        }

        return chatRepository.save(group);
    }

    /**
     * Adds a user to a group chat.
     *
     * @param userId the ID of the user to add
     * @param chatId the ID of the group chat
     * @param requestUser the user making the request
     * @return the updated group chat
     * @throws UserException if the request user is not an admin
     * @throws ChatException if the chat cannot be found
     */
    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, AppUser requestUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat with id: " + chatId + " not found."));

        AppUser user = userService.findUserById(userId);

        if (chat.getAdmins().contains(requestUser)) {
            chat.getUsers().add(user);
            return chatRepository.save(chat);
        } else {
            throw new UserException("Only admins are allowed to add members to group");
        }
    }

    /**
     * Renames a group chat.
     *
     * @param chatId the ID of the group chat
     * @param groupName the new group name
     * @param requestUser the user making the request
     * @return the updated group chat
     * @throws UserException if the request user is not a member of the group
     * @throws ChatException if the chat cannot be found
     */
    @Override
    public Chat renameGroup(Integer chatId, String groupName, AppUser requestUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat with id: " + chatId + " not found"));

        if (chat.getUsers().contains(requestUser)) {
            chat.setName(groupName);
            return chatRepository.save(chat);
        } else {
            throw new UserException("Only members of the chat group can modify the group name");
        }
    }

    /**
     * Removes a user from a group chat.
     *
     * @param chatId the ID of the group chat
     * @param userId the ID of the user to remove
     * @param requestUser the user making the request
     * @return the updated group chat
     * @throws UserException if the request user is not an admin or is not the user being removed
     * @throws ChatException if the chat cannot be found
     */
    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, AppUser requestUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat with id: " + chatId + " not found."));

        AppUser userToRemove = userService.findUserById(userId);

        if (chat.getAdmins().contains(requestUser)) {
            chat.getUsers().remove(userToRemove);
            return chatRepository.save(chat);
        } else if (chat.getUsers().contains(requestUser)) {
            if (userId.equals(requestUser.getId())) {
                chat.getUsers().remove(requestUser);
                return chatRepository.save(chat);
            } else {
                throw new UserException("Cannot remove other users");
            }
        } else {
            throw new UserException("Only admins are allowed to add members to group");
        }
    }

    /**
     * Deletes a chat by its ID.
     *
     * @param chatId the ID of the chat to delete
     * @throws UserException if the user is not authorized to delete the chat
     * @throws ChatException if the chat cannot be found
     */
    @Override
    public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat with id: " + chatId + " not found"));

        chatRepository.deleteById(chatId);
    }

    /**
     * Retrieves chat details by its ID.
     *
     * @param chatId the ID of the chat
     * @return the chat details DTO
     */
    @Override
    public ChatDetailsDto getChatDetails(Integer chatId) {
        Chat chat = chatRepository.findChatDetails(chatId);

        if (chat == null) {
            throw new ChatException("Unable to fetch chat details for chat: " + chatId);
        }

        ChatDetailsDto response = new ChatDetailsDto();
        response.setId(chat.getId());

        // Log the value of isGroup
        boolean isGroup = chat.isGroup();
        System.out.println("isGroup: " + isGroup);

        if (isGroup) {
            response.setName(chat.getName());
        } else {
            response.setName(getChatSecondUser(chatId).getFullName());
        }

        response.setImage(chat.getImage());
        response.setUsers(chat.getUsers());

        return response;
    }

    @Override
    public Chat updateGroup(Integer chatId, UpdateGroupRequest request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(
                        () -> new ChatException("Chat with id: " + chatId + " not found.")
                );

        if (request.getGroupName() == null && request.getGroupImage() != null)
            chat.setImage(request.getGroupImage());
        else if (request.getGroupImage() == null && request.getGroupName() != null)
            chat.setName(request.getGroupName());
        else if (request.getGroupName() != null && request.getGroupImage() != null) {
            chat.setName(request.getGroupName());
            chat.setImage(request.getGroupImage());
        }

        return chatRepository.save(chat);
    }

    /**
     * Fetches the other user in a chat for display purposes.
     *
     * @param chatId the ID of the chat
     * @return the other user in the chat
     */
    private AppUser getChatSecondUser(Integer chatId) {
        AppUser loggedInUser = userService.findUserProfile();

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        return chat.getUsers().get(0).equals(loggedInUser) ? chat.getUsers().get(1) : chat.getUsers().get(0);
    }
}
