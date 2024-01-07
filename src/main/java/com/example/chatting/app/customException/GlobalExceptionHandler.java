package com.example.chatting.app.customException;
import com.example.chatting.app.customException.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(NotFoundException ex, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IsPresentException.class)
    public ResponseEntity<ErrorObject> handleIsPresentException(IsPresentException ex, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotMatchException.class)
    public ResponseEntity<ErrorObject> handleNotMatchException(NotMatchException ex, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.CONFLICT.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorObject> handleForbiddenException(ForbiddenException ex, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(UnAcceptableException.class)
    public ResponseEntity<ErrorObject> handleUnAcceptableException(UnAcceptableException ex, WebRequest request){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    String fieldName = ((FieldError)error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(fieldName,message);
                }
        );
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }


}
