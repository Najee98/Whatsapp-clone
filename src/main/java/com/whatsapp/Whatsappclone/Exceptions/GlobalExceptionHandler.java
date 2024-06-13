package com.whatsapp.Whatsappclone.Exceptions;

import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.ChatException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.MessageException;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * GlobalExceptionHandler handles exceptions thrown by the application.
 * It provides a centralized way to manage and respond to different types of errors.
 */
@RestControllerAdvice // This annotation makes the class handle exceptions globally across the application.
public class GlobalExceptionHandler {

    /**
     * Handles UserException specifically.
     *
     * @param e The UserException thrown.
     * @param request The web request that caused the exception.
     * @return A ResponseEntity with an error message and HTTP status.
     */
    @ExceptionHandler(UserException.class) // Specifies that this method handles UserException.
    public ResponseEntity<ErrorResponse> handleUserException(UserException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles MessageException specifically.
     *
     * @param e The MessageException thrown.
     * @param request The web request that caused the exception.
     * @return A ResponseEntity with an error message and HTTP status.
     */
    @ExceptionHandler(MessageException.class) // Specifies that this method handles MessageException.
    public ResponseEntity<ErrorResponse> handleMessageException(MessageException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles ChatException specifically.
     *
     * @param e The ChatException thrown.
     * @param request The web request that caused the exception.
     * @return A ResponseEntity with an error message and HTTP status.
     */
    @ExceptionHandler(ChatException.class) // Specifies that this method handles ChatException.
    public ResponseEntity<ErrorResponse> handleChatException(ChatException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles general exceptions that are not covered by specific handlers.
     *
     * @param e The Exception thrown.
     * @param request The web request that caused the exception.
     * @return A ResponseEntity with an error message and HTTP status.
     */
    @ExceptionHandler(Exception.class) // Specifies that this method handles all other exceptions.
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
