package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        LOGGER.info("Listing all users");
        List<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers);
        return "fragments/users";
    }

    @GetMapping("/users/new")
    public String createUser(Model model) {
        LOGGER.info("Displaying form to create a new user");
        User user = new User();
        List<Role> listRoles = userService.listRoles();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "fragments/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("listRoles", userService.listRoles());
            return "fragments/user_form";
        }

        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(userService.getRoleById(role.getId()));
        }
        user.setRoles(roles);

        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "User saved successfully");
        return "redirect:/users";
    }

}