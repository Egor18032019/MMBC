package com.mmbc.demo.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseEntity<Error> handleException400(HttpServletRequest request,
                                                    NoHandlerFoundException exception) {
        System.out.println("       handleException400");
        return new ResponseEntity<>(new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleException(BadRequestException exception) {
        System.out.println(exception.getMessage());
        return new ResponseEntity<>(new Error(400, exception.getMessage()), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler
    public ResponseEntity<Error> handleException(MissingPathVariableException exception) {
        System.out.println(exception.getMessage() +  "ПУТЬ ");
        return new ResponseEntity<>(new Error(400, "MissingPathVariableException"), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleException(HttpMessageNotReadableException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(new Error(400, "bad request"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleException(NotFoundException exception) {
        System.out.println(exception.getMessage());
        return new ResponseEntity<>(new Error(404, exception.getMessage()), HttpStatus.NOT_FOUND);

    }


}
