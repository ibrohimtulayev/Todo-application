package com.pdp.taskmanagerapp.controller;

import com.pdp.taskmanagerapp.entity.Column;
import com.pdp.taskmanagerapp.entity.Task;
import com.pdp.taskmanagerapp.entity.User;
import com.pdp.taskmanagerapp.repo.ColumnRepo;
import com.pdp.taskmanagerapp.repo.TaskRepo;
import com.pdp.taskmanagerapp.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ColumnRepo columnRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TaskRepo taskRepo;

    @GetMapping("/")
    public String redirectToPage(Model model,
                                 @RequestParam(required = false, defaultValue = "false") Boolean showMyTasks) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepo.findByUsername(username);

        List<Task> tasks;

        if (showMyTasks && user != null) {
            tasks = taskRepo.findAllByUsersContaining(user);
        } else {
            tasks = taskRepo.findAll();
        }
        model.addAttribute("greatest", columnRepo.findGreatestOrderNumber());
        model.addAttribute("smallest", columnRepo.findSmallestOrderNumber());
        model.addAttribute("columns", columnRepo.findAllByOrderNumber());
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);
        return "home";
    }


    @GetMapping("/report1")
    public String report1(Model model) {
        List<User> users = userRepo.findAll();
        for (User user : users) {
            long overdueTasks = taskRepo.countByUsersContainingAndIsOverdue(user.getId());
            long successfulTasks = taskRepo.countByUsersContainingAndIsCompletedOnTime(user.getId());
            long allTasks = taskRepo.countByUsersContaining(user);

            user.setOverdueTasks((int) overdueTasks);
            user.setSuccessfulTasks((int) successfulTasks);
            user.setAllTasks((int) allTasks);
        }
        model.addAttribute("users", users);
        return "report1";
    }

    @GetMapping("/report2")
    public String report2(Model model) {
        model.addAttribute("columns", columnRepo.findAll());
        return "report2";
    }

    @GetMapping("/registration")
    public String registration() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password) {
        userRepo.save(User.builder()
                .username(username)
                        .avatar("https://img.freepik.com/premium-photo/cartoon-character-with-blue-shirt-glasses_561641-2084.jpg?size=626&ext=jpg&ga=GA1.1.676323623.1713357036&semt=sph")
                .role("USER")
                .password(passwordEncoder.encode(password))
                .build());

        return "redirect:/";
    }
}
