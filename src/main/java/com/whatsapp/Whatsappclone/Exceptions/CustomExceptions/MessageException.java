package com.whatsapp.Whatsappclone.Exceptions.CustomExceptions;

public class MessageException extends RuntimeException{

    public MessageException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public MessageException(String exceptionMessage, Throwable exceptionCause) {
        super(exceptionMessage, exceptionCause);
    }

}
