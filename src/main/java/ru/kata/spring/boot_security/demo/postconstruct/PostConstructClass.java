package ru.kata.spring.boot_security.demo.postconstruct;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class PostConstructClass {
    private final UserService userService;

    public PostConstructClass(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        userService.saveRole(adminRole);
        userService.saveRole(userRole);

        Set<Role> rolesForAdmin = new HashSet<>();
        rolesForAdmin.add(userRole);
        rolesForAdmin.add(adminRole);

        Set<Role> rolesForUser = new HashSet<>();
        rolesForUser.add(userRole);

        User admin = new User("admin", "admin", (byte) 35, "admin@mail.ru", "admin", rolesForAdmin);
        User user = new User("user", "user", (byte) 30, "user@mail.ru", "user", rolesForUser);

        userService.saveUser(admin);
        userService.saveUser(user);
    }
}
