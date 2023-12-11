package world.evgereo.articles.models;

import jakarta.validation.constraints.*;

public class Users {
    private int userId;
    @NotBlank(message = "Name should be not empty or blank")
    @Size(min=2, max=100, message = "Size of your name too short or long")
    private String userName;
    @NotBlank(message = "Surname should be not empty or blank")
    @Size(min=2, max=100, message = "Size of your surname too short or long")
    private String userSurname;
    @Min(0)
    private int age;
    @NotBlank(message = "Email should be not empty")
    @Email(message = "Please provide a valid email address")
    @Size(max=150, message = "Maximum size of email is 150")
    private String email;
    @Size(min=2, max=100, message = "Size of your password too short or long")
    private String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserId" + getUserId();
    }
}
