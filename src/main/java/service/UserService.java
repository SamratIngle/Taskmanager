package service;



import model.User;
import repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String firstName, String lastName, String timeZone, boolean isActive) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setTimeZone(timeZone);
        user.setActive(isActive);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, String firstName, String lastName, String timeZone, boolean isActive) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setTimeZone(timeZone);
        user.setActive(isActive);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
