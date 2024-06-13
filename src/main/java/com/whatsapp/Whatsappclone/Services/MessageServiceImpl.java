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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    /**
     * Sends a message in a chat.
     *
     * @param request the message request details
     * @param fromUser the user sending the message
     * @return the last message in the chat after the new message is sent
     * @throws UserException if there is a user-related error
     * @throws ChatException if the chat cannot be found
     */
    @Override
    public Message sendMessage(SendMessageRequest request, AppUser fromUser) throws UserException, ChatException {
        Chat chat = chatService.findChatById(request.getChatId());

        List<AppUser> receivers = userService.findChatTargetUser(chat.getId(), fromUser);

        Message message = new Message();
        message.setFromUser(fromUser);
        message.setChat(chat);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());

        if (!chat.isGroup()) {
            message.setUser(receivers.get(0));
        }

        messageRepository.save(message);

        return messageRepository.getLastMessageInChat(chat);
    }

    /**
     * Retrieves all messages in a chat for a specific user.
     *
     * @param chatId the ID of the chat
     * @param requestUser the user requesting the messages
     * @return a list of chat messages DTOs
     * @throws ChatException if the chat cannot be found
     * @throws UserException if the user is not in the chat
     */
    @Override
    public List<ChatMessagesDto> getChatMessages(Integer chatId, AppUser requestUser) throws ChatException {
        List<ChatMessagesDto> messages = new ArrayList<>();

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found"));

        if (!chat.getUsers().contains(requestUser)) {
            throw new UserException("User is not in this chat.");
        }

        AppUser user1 = chat.getUsers().get(0);
        AppUser user2 = chat.getUsers().get(1);

        if (!chat.isGroup()) {
            messages = messageRepository.findChatMessages(chatId, user1, user2);
        } else {
            messages = messageRepository.findChatMessagesForGroup(chatId, user1);
        }

        return messages;
    }

    /**
     * Deletes a message.
     *
     * @param messageId the ID of the message to delete
     * @param requestUser the user requesting the deletion
     * @throws MessageException if the message cannot be found
     * @throws UserException if the user is not authorized to delete the message
     */
    @Override
    public void deleteMessage(Integer messageId, AppUser requestUser) throws MessageException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageException("Message with id: " + messageId + " not found."));

        if (message.getUser().equals(requestUser.getId())) {
            messageRepository.deleteById(messageId);
        } else {
            throw new UserException("Cannot delete another user's message.");
        }
    }

    /**
     * Gets the content of the last message in a chat for a specific user.
     *
     * @param chatId the ID of the chat
     * @param user the user requesting the last message content
     * @return the content of the last message
     */
    @Override
    public String getLastMessageContentForChat(Integer chatId, AppUser user) {
        List<Message> messages = messageRepository.findTopByChatIdOrderByTimestampDesc(chatId);
        if (!messages.isEmpty()) {
            return messages.get(0).getContent();
        } else {
            return ""; // or any default value you want to use
        }
    }

    /**
     * Gets the timestamp of the last message in a chat for a specific user.
     *
     * @param chatId the ID of the chat
     * @param user the user requesting the last message timestamp
     * @return the timestamp of the last message
     */
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
