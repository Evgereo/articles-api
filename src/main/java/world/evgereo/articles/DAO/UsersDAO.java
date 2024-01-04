package world.evgereo.articles.DAO;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import world.evgereo.articles.models.Users;

import java.util.List;

//@Component
//public class UsersDAO {
//    private final JdbcTemplate jdbcTemplate;
//
//    public UsersDAO(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<Users> getUsers() {
//        return jdbcTemplate.query("SELECT * FROM users ORDER BY user_id", new BeanPropertyRowMapper<>(Users.class));
//    }
//
//    public Users getUser(int id) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?",
//                new BeanPropertyRowMapper<>(Users.class), id);
//    }
//
//    public void postUser(Users user) {
//        jdbcTemplate.update("INSERT INTO users(user_name, user_surname, age, email, password) VALUES(?, ?, ?, ?, ?)",
//                user.getUserName(), user.getUserSurname(), user.getAge(), user.getEmail(), user.getPassword());
//    }
//
//    public void patchUser(Users user, int id) {
//        jdbcTemplate.update("UPDATE users SET user_name = ?, user_surname = ?, age = ?, email = ? WHERE user_id = ?",
//                user.getUserName(), user.getUserSurname(), user.getAge(), user.getEmail(), id);
//    }
//
//    public void deleteUser(int id) {
//        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
//    }
//}