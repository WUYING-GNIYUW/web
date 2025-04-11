package authorizationServer.exceptionHandler;

import authorizationServer.exception.LoginException;
import common.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result<String> JwtExceptionHandler(LoginException loginException) {
        return Result.<String>builder().data(loginException.getMessage()).build();
    }
}
