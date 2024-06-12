package com.whatsapp.Whatsappclone.Services;

import com.whatsapp.Whatsappclone.Dto.ChatMessagesDto;
import com.whatsapp.Whatsappclone.Dto.SendMessageRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.ChatException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.MessageException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;
import com.whatsapp.Whatsappclone.Models.Message;
import com.whatsapp.Whatsappclone.Repositories.ChatRepository;
import com.whatsapp.Whatsappclone.Repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final ChatRepository chatRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserService userService, @Lazy ChatService chatService, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatService = chatService;
        this.chatRepository = chatRepository;
    }

//    @Override
//    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        AppUser requestUser = (AppUser) authentication.getPrincipal();
//
//        AppUser targetUser = userService.findUserById(request.getUserId());
//
//        Chat chat = chatService.findChatById(request.getChatId());
//
//        Message message = new Message();
//
//        chat.getUsers().add(requestUser);
//        chat.getUsers().add(targetUser);
//
//        message.setChat(chat);
//        message.setUser(targetUser);
//        message.setContent(request.getContent());
//        message.setTimestamp(LocalDateTime.now());
//
//        messageRepository.save(message);
//
//        return message;
//    }
//
//    @Override
//    public List<ChatMessagesDto> getChatMessages(Integer chatId, AppUser requestUser) throws ChatException {
//
//        Chat chat = chatRepository.findById(chatId).get();
//
//        if (!chat.getUsers().contains(requestUser))
//            throw new UserException("User is not in this chat.");
//
//        AppUser user1 = chat.getUsers().get(0);
//        AppUser user2 = chat.getUsers().get(1);
////        List<Message> messages = messageRepository.findByChatId(chatId);
//
//        List<ChatMessagesDto> messages = messageRepository.findChatMessages(chatId, user1, user2);
//
//        return messages;
//    }

    @Override
    public Message sendMessage(SendMessageRequest request, AppUser fromUser) throws UserException, ChatException {

        Chat chat = chatService.findChatById(request.getChatId());

        List<AppUser> receivers = userService.findChatTargetUser(chat.getId(), fromUser);

        Message message = new Message();

        message.setFromUser(fromUser);
        message.setChat(chat);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());

        if (!chat.isGroup() == true){
            message.setUser(receivers.get(0));
        }

        messageRepository.save(message);

        return messageRepository.getLastMessageInChat(chat);
    }

    @Override
    public List<ChatMessagesDto> getChatMessages(Integer chatId, AppUser requestUser) throws ChatException {

        List<ChatMessagesDto> messages = new ArrayList<>();

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(
                        () -> new ChatException("Chat not found")
                );

        if (!chat.getUsers().contains(requestUser))
            throw new UserException("User is not in this chat.");

        AppUser user1 = chat.getUsers().get(0);
        AppUser user2 = chat.getUsers().get(1);

        if (!chat.isGroup())
            messages = messageRepository.findChatMessages(chatId, user1, user2);
        else{
            messages = messageRepository.findChatMessagesForGroup(chatId, user1);

        }

        return messages;
    }


    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageException("Message with id: " + messageId + " not found."));

        return message;
    }

    @Override
    public void deleteMessage(Integer messageId, AppUser requestUser) throws MessageException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageException("Message with id: " + messageId + " not found."));

        if (message.getUser().equals(requestUser.getId()))
            messageRepository.deleteById(messageId);

        throw new UserException("Cannot delete another user message.");
    }

    @Override
    public String getLastMessageContentForChat(Integer chatId, AppUser user) {
        List<Message> messages = messageRepository.findTopByChatIdOrderByTimestampDesc(chatId);
        if (!messages.isEmpty()) {
            return messages.get(0).getContent();
        } else {
            return ""; // or any default value you want to use
        }
    }

    @Override
    public LocalDateTime getLastMessageTimeStampForChat(Integer chatId, AppUser user) {
        List<Message> messages = messageRepository.findTopByChatIdOrderByTimestampDesc(chatId);
        if (!messages.isEmpty()) {
            return messages.get(0).getTimestamp();
        } else {
            return LocalDateTime.MIN;
        }
    }
}
