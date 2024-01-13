package world.evgereo.articles.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import world.evgereo.articles.models.Users;
import world.evgereo.articles.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersService {
    private UsersRepository usersRepository;

    public List<Users> getUsers() {
        return usersRepository.findAll();
    }

    public Users getUser(int id) {
        Optional<Users> user = usersRepository.findById(id);
        return user.orElse(null);
    }

    public void updateUser(Users user, int id) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
            existingUser.setUserName(user.getUserName());
            existingUser.setUserSurname(user.getUserSurname());
            existingUser.setAge(user.getAge());
            existingUser.setEmail(user.getEmail());
            usersRepository.save(existingUser);
        }
    }

    public void createUser(Users user) {
        usersRepository.save(user);
    }

    public void deleteUser(int id) {
        usersRepository.deleteById(id);
    }
}
