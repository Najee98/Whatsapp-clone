package com.whatsapp.Whatsappclone.Exceptions.CustomExceptions;

public class UserException extends RuntimeException{

    public UserException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public UserException(String exceptionMessage, Throwable exceptionCause) {
        super(exceptionMessage, exceptionCause);
    }

}
