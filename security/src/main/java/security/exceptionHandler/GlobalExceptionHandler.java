package security.exceptionHandler;

import security.exception.LoginException;
import security.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result JwtExceptionHandler(LoginException loginException) {
        return Result.<String>builder().data(loginException.getMessage()).build();
    }
}
