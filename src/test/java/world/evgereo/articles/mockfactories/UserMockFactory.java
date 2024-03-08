package world.evgereo.articles.mockfactories;

import world.evgereo.articles.dtos.RegistrationUserDto;
import world.evgereo.articles.dtos.UpdateUserDto;
import world.evgereo.articles.models.Role;
import world.evgereo.articles.models.User;

import java.util.List;
import java.util.Set;

public class UserMockFactory {
    private static final User firstUser =
            new User(
                    1,
                    "test",
                    "first",
                    10,
                    "testfirst@gmail.com",
                    "password",
                    Set.of(new Role(1, "ROLE_USER")));

    private static final User secondUser =
            new User(
                    2,
                    "test",
                    "third",
                    20,
                    "testthird@gmail.com",
                    "password",
                    Set.of(new Role(1, "ROLE_USER"),
                            new Role(2, "ROLE_ADMIN")));

    private static final User thirdUser =
            new User(
                    3,
                    "test",
                    "third",
                    30,
                    "testthird@gmail.com",
                    "password",
                    Set.of(new Role(1, "ROLE_USER"),
                            new Role(2, "ROLE_ADMIN"),
                            new Role(3, "ROLE_MODER")));

    private static final User fourthUser =
            new User(
                    4,
                    "test",
                    "fourth",
                    40,
                    "testfourth@gmail.com",
                    "password",
                    Set.of(new Role(1, "ROLE_USER"),
                            new Role(2, "ROLE_ADMIN"),
                            new Role(3, "ROLE_MODER"),
                            new Role(4, "ROLE_OWNER")));

    public static User getFirstUser() {
        return UserMockFactory.firstUser;
    }

    public static List<User> getListOfTwoUsers() {
        return List.of(UserMockFactory.firstUser, UserMockFactory.fourthUser);
    }

    public static RegistrationUserDto getValidRegistrationUserDTO() {
        return new RegistrationUserDto(
                "test",
                "first",
                10,
                "testfirst@gmail.com",
                "password",
                "password"
        );
    }

    public static RegistrationUserDto getInvalidRegistrationUserDTO() {
        RegistrationUserDto dto = UserMockFactory.getValidRegistrationUserDTO();
        dto.setPasswordConfirm("notpassword");
        return dto;
    }

    public static UpdateUserDto getUpdateUserDTO() {
        return new UpdateUserDto(
                "test",
                "first",
                10,
                "testfirst@gmail.com");
    }
}
