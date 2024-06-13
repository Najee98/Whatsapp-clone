package com.whatsapp.Whatsappclone.Exceptions.CustomExceptions;

/**
 * UserException is a custom exception that represents errors specific to user operations.
 * It extends RuntimeException, so it is unchecked and can be thrown during runtime without being declared.
 */
public class UserException extends RuntimeException {

    /**
     * Constructs a new UserException with the specified detail message.
     *
     * @param exceptionMessage The detail message explaining the reason for the exception.
     */
    public UserException(String exceptionMessage) {
        super(exceptionMessage); // Calls the superclass constructor with the provided message.
    }

    /**
     * Constructs a new UserException with the specified detail message and cause.
     *
     * @param exceptionMessage The detail message explaining the reason for the exception.
     * @param exceptionCause The cause of the exception, which can be retrieved later using the getCause() method.
     */
    public UserException(String exceptionMessage, Throwable exceptionCause) {
        super(exceptionMessage, exceptionCause); // Calls the superclass constructor with the provided message and cause.
    }
}
