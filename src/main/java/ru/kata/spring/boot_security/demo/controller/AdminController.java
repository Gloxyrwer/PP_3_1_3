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
    public String showAdminForm(Model model) {
        log.info("Show admin form");
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/add")
    public String showCreateForm(Model model) {
        log.info("Show create form");
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin-createUser";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("id") long id, Model model) {
        log.info("Show update form");
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin-updateUser";
    }

    @PostMapping({"/add", "/update"})
    public String saveUser(@ModelAttribute("user") User user) {
        log.info("Saving user: {}", user);
        userService.saveUser(user);
        log.info("User saved: {}", user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") long id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteUser(userService.getUserById(id));
        log.info("User deleted: {}", id);
        return "redirect:/admin";
    }
}
