package com.whatsapp.Whatsappclone.Services;

import com.whatsapp.Whatsappclone.Dto.ChatsIndexDto;
import com.whatsapp.Whatsappclone.Dto.GroupChatRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.ChatException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;
import com.whatsapp.Whatsappclone.Repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageService messageService;

    @Override
    public Chat createChat(AppUser requestUser, Integer targetUserId) throws UserException {
        AppUser targetUser = userService.findUserById(targetUserId);

        Chat isChatExist = chatRepository.findChatByUsers(requestUser, targetUser);

        if (isChatExist != null)
            return isChatExist;

        Chat chat = new Chat();
        chat.setName(
                userService.findUserById(targetUserId).getFullName()
        );
        chat.setImage(
                userService.findUserById(targetUserId).getProfilePicture()
        );
        chat.setCreatedBy(requestUser);
        chat.getUsers().add(requestUser);
        chat.getUsers().add(targetUser);
        chat.setGroup(false);

        return chatRepository.save(chat);
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = Optional.ofNullable(chatRepository.findById(chatId)
                .orElseThrow(
                        () -> new ChatException("Chat with id: " + chatId + " not found")
                ));

        return chat.get();
    }

    @Override
    public List<ChatsIndexDto> findAllChatsByUserId(Integer userId) {
        AppUser user = userService.findUserById(userId);
        List<Object[]> chatDataList = chatRepository.findChatDataByUserId(user);
        List<ChatsIndexDto> chats = new ArrayList<>();

        for (Object[] chatData : chatDataList) {
            Integer chatId = (Integer) chatData[0];
            String chatName = (String) chatData[1];
            String chatImage = (String) chatData[2];
            boolean isGroup = (boolean) chatData[3];

            String lastMessageContent = messageService.getLastMessageContentForChat(chatId, user);
            ChatsIndexDto dto = new ChatsIndexDto(chatId, chatName, chatImage, isGroup, lastMessageContent);
            chats.add(dto);
        }

        return chats;
    }

    @Override
    public Chat createChatGroup(GroupChatRequest request, AppUser requestUser) throws UserException {
        Chat group = new Chat();

        group.setGroup(true);
        group.setImage(request.getChatImage());
        group.setName(request.getChatName());
        group.setCreatedBy(requestUser);
        group.getAdmins().add(requestUser);

        for(Integer userId : request.getUserIds()){
            AppUser groupUser = userService.findUserById(userId);
            group.getUsers().add(groupUser);
        }
        group.getUsers().add(requestUser);

        return chatRepository.save(group);
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, AppUser requestUser) throws UserException, ChatException {
        Optional<Chat> chat = Optional.ofNullable(chatRepository.findById(chatId)
                .orElseThrow(
                        () -> new ChatException("Chat with id: " + chatId + " not found.")
                ));

        AppUser user = userService.findUserById(userId);

        if (chat.get().getAdmins().contains(requestUser)){
            chat.get().getUsers().add(user);

            return chat.get();
        } else {
            throw new UserException("Only admins are allowed to add members to group");
        }

    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, AppUser requestUser) throws UserException, ChatException {
        Optional<Chat> chat = Optional.ofNullable(chatRepository.findById(chatId)
                .orElseThrow(
                        () -> new ChatException("Chat with id: " + chatId + " not found")
                ));

        if (chat.get().getUsers().contains(requestUser)){
            chat.get().setName(groupName);
            chatRepository.save(chat.get());
            return chat.get();
        }else {
            throw new UserException("Only members of the chat group can modify the group name");
        }

    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, AppUser requestUser) throws UserException, ChatException {
        Optional<Chat> chat = Optional.ofNullable(chatRepository.findById(chatId)
                .orElseThrow(
                        () -> new ChatException("Chat with id: " + chatId + " not found.")
                ));

        AppUser userToRemove = userService.findUserById(userId);

        if (chat.get().getAdmins().contains(requestUser)){
            chat.get().getUsers().remove(userToRemove);
            return chatRepository.save(chat.get());
        }
        else if (chat.get().getUsers().contains(requestUser)) {
            if (userId == requestUser.getId()){
                chat.get().getUsers().remove(requestUser);
                return chatRepository.save(chat.get());
            }
            else {
                throw new UserException("Cannot remove other users");
            }
        }
        else {
            throw new UserException("Only admins are allowed to add members to group");
        }
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(
                        () -> new ChatException("Chat with id: " + chatId + " not found")
                );

        chatRepository.deleteById(chatId);
    }

}
