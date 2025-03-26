package Security.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Result<T> {
    private String code;
    private String message;
    private T data;
}




