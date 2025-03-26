package Security.pojo;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private String userid;
    private String password;
    private String username;
    private Integer availableState;
    private Integer onlineState;
    private String roles;
    private String origin;
}
