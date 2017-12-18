package slackhubapi.demo.Controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import slackhubapi.demo.Models.User;
import slackhubapi.demo.Repositories.UserRepository;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public List<User> listAllUsers(){
        return userRepository.findAll();
    }

    @RequestMapping(value = "users", method = RequestMethod.POST)
    public User createUser(@RequestBody User user){
        return userRepository.saveAndFlush(user);
    }

    @RequestMapping(value = "users/{userId}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Long userId){
        return userRepository.findOne(userId);
    }

    @RequestMapping(value = "users/{userId}", method = RequestMethod.PUT)
    public User updateUserById(@PathVariable Long userId, @RequestBody User user) {
        User existingUser = getUserById(userId);
        BeanUtils.copyProperties(user, existingUser);
        return userRepository.saveAndFlush(existingUser);
    }

    @RequestMapping(value = "users/{userId}", method = RequestMethod.DELETE)
    public User deleteUserById(@PathVariable Long userId) {
        User existingUser = getUserById(userId);
        userRepository.delete(existingUser);
        return existingUser;
    }
}
