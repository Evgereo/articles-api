package world.evgereo.articles.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private int userId;
    private String userName;
    private String userSurname;
    private int age;
    private String email;
    private String password;
    private String passwordConfirm;
}
