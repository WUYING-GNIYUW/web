package com.wuying.userServer.exceptionHandler;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler
//    public Result JwtExceptionHandler(LoginException loginException) {
//        return Result.<String>builder().data(loginException.getMessage()).build();
//    }
}
