package com.whatsapp.Whatsappclone.Exceptions.CustomExceptions;

public class ChatException extends RuntimeException{

    public ChatException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public ChatException(String exceptionMessage, Throwable exceptionCause) {
        super(exceptionMessage, exceptionCause);
    }

}
