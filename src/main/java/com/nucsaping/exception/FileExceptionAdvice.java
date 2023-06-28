package com.nucsaping.exception;

import com.nucsaping.message.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException e) {

        String message = "File too large";
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.EXPECTATION_FAILED.value(),
                message,
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(responseMessage, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMessage> handleImageNotFoundException(ImageNotFoundException e) {

        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }
}
