package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));

        return user;
    }

    public void saveUser(User user) {
        Role role = new Role("ROLE_USER");
        roleRepository.save(role);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
    }

    public void saveUser(User user, Set<Role> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public void deleteUser(long id) {
        if (userRepository.existsById(id))
            userRepository.deleteById(id);
        else
            throw new UsernameNotFoundException(String.format("User with id='%d' not found", id));
    }

    public void updateUser(User updatedUser) {
        User user = getUserById(updatedUser.getId());

        user.setUsername(updatedUser.getUsername());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            user.setRoles(updatedUser.getRoles());
            roleRepository.saveAll(user.getRoles());
        }

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }
}
