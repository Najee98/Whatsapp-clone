package com.whatsapp.Whatsappclone.Services;

import com.whatsapp.Whatsappclone.Dto.ChatMessagesDto;
import com.whatsapp.Whatsappclone.Dto.SendMessageRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.ChatException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.MessageException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Message;

import java.util.List;

public interface MessageService {

    Message sendMessage(SendMessageRequest request) throws UserException, ChatException;
    
    List<ChatMessagesDto> getChatMessages(Integer chatId, AppUser requestUser) throws ChatException;

    Message findMessageById(Integer messageId) throws MessageException;

    void deleteMessage(Integer messageId, AppUser requestUser) throws MessageException;

}
