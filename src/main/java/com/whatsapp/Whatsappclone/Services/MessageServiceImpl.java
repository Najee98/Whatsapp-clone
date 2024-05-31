package com.whatsapp.Whatsappclone.Services;

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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final ChatRepository chatRepository;

    @Override
    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException {
        AppUser user = userService.findUserById(request.getUserId());
        Chat chat = chatService.findChatById(request.getChatId());

        Message message = new Message();

        message.setChat(chat);
        message.setUser(user);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());

        return message;
    }

    @Override
    public List<Message> getChatMessages(Integer chatId, AppUser requestUser) throws ChatException {

        Chat chat = chatRepository.findById(chatId).get();

        if (!chat.getUsers().contains(requestUser))
            throw new UserException("User is not in this chat.");

        List<Message> messages = messageRepository.findByChatId(chatId);

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
}
