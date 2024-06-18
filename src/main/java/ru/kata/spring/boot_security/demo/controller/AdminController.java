package ru.kata.spring.boot_security.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        log.info("Show admin page");
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/add")
    public String showCreateForm() {
        log.info("Show create form");
        return "admin-createUser";
    }

    @PostMapping("/add")
    public String createUser(@ModelAttribute("user") User user) {
        log.info("Creating user: {}", user);
        userService.saveUser(user);
        log.info("User created: {}", user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("id") long id, Model model) {
        log.info("Show update form with id: {}", id);
        model.addAttribute("user", userService.getUserById(id));
        return "admin-updateUser";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        log.info("Updating user: {}", user);
        userService.updateUser(user);
        log.info("User updated: {}", user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") long id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
        log.info("User deleted: {}", id);
        return "redirect:/admin";
    }
}
