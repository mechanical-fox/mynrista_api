package app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;



import app.exception.BadRequestException;
import app.exception.NotFoundException;
import app.exception.UnauthorizedException;


@RestControllerAdvice
public class ExceptionAdviser {

    
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String BadRequestHandler(BadRequestException err) {
        return err.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String NotFoundHandler(NotFoundException err) {
        return err.getMessage();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String UnauthorizedHandler(UnauthorizedException err) {
        return err.getMessage();
    }
}



