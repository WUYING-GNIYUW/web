package com.wuying.authorizationServer.exceptionHandler;

import com.wuying.authorizationServer.exception.LoginException;
import com.wuying.common.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result<String> JwtExceptionHandler(LoginException loginException) {
        return Result.<String>builder().data(loginException.getMessage()).build();
    }
}
