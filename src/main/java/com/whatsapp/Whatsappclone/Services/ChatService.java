package com.whatsapp.Whatsappclone.Services;

import com.whatsapp.Whatsappclone.Dto.ChatDetailsDto;
import com.whatsapp.Whatsappclone.Dto.ChatsIndexDto;
import com.whatsapp.Whatsappclone.Dto.GroupChatRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.ChatException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;

import java.util.List;

public interface ChatService {

    Chat createChat(AppUser requestUser, Integer targetUserId) throws UserException;

    Chat findChatById(Integer chatId) throws ChatException;

    List<ChatsIndexDto> findAllChatsByUserId(Integer userId);

    Chat createChatGroup(GroupChatRequest request, AppUser user) throws UserException;

    Chat addUserToGroup(Integer userId, Integer chatId, AppUser requestUser) throws UserException, ChatException;

    Chat renameGroup(Integer chatId, String groupName, AppUser requestUser) throws UserException, ChatException;

    Chat removeFromGroup(Integer chatId, Integer userId, AppUser requestUser) throws UserException, ChatException;

    void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException;

    ChatDetailsDto getChatDetails(Integer chatId);
}
