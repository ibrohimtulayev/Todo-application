package com.pdp.taskmanagerapp.controller;

import com.pdp.taskmanagerapp.entity.*;
import com.pdp.taskmanagerapp.repo.*;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {


    private final TaskRepo taskRepo;

    private final ColumnRepo columnRepo;

    private final UserRepo userRepo;
    private final  AttachmentRepo attachmentRepo;
    private final AttachmentContentRepo attachmentContentRepo;



    @PostMapping("/add")
    public String addTask(@RequestParam("columnId") Integer columnId,
                          @RequestParam("taskName") String taskName) {
        Column column = columnRepo.findById(columnId).orElseThrow(() -> new IllegalArgumentException("Column not found"));

        Task task = Task.builder().name(taskName).column(column)
                .deadLine(LocalDate.now())
                .build();
        taskRepo.save(task);

        column.getTasks().add(task);
        columnRepo.save(column);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Integer id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Column column = task.getColumn();
        column.getTasks().remove(task);
        columnRepo.save(column);

        taskRepo.delete(task);
        return "redirect:/";
    }


    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Integer id, Model model) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        List<Comment> comments = task.getComments();

        model.addAttribute("task", task);
        List<User> taskUsers = task.getUsers();
        List<User> allUsers = userRepo.findAll();

        List<User>usersToChoose = new ArrayList<>(allUsers);
        usersToChoose.removeAll(taskUsers);

        List<Attachment> attachments = task.getAttachments();


        model.addAttribute("attachments", attachments);
        model.addAttribute("comments", comments);
        model.addAttribute("usersToChoose", usersToChoose);
        model.addAttribute("users",usersToChoose);
        model.addAttribute("taskUsers", task.getUsers());
        model.addAttribute("columns", columnRepo.findAll());
        return "task";
    }
    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Integer id,
                             @RequestParam String taskName,
                             @RequestParam LocalDate deadline,
                             @RequestParam Integer columnId,
                             @RequestParam(required = false) Integer userId,
                             @RequestParam(name = "file") MultipartFile file) throws IOException {
        Task task = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setName(taskName);
        task.setDeadLine(deadline);

        if (file != null && !file.isEmpty()) { // Check if file is not null and not empty
            // Create attachment
            Attachment attachment = new Attachment();
            attachment.setName(file.getOriginalFilename());
            attachment.setContentType(file.getContentType());
            // Save attachment and set attachment content
            attachment = attachmentRepo.save(attachment);
            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setAttachment(attachment);
            attachmentContent.setContent(file.getBytes());
            attachmentContentRepo.save(attachmentContent);
            // Add attachment to task
            List<Attachment> attachments = task.getAttachments();
            attachments.add(attachment);
            task.setAttachments(attachments);
        }

        // Update column if changed
        if (!task.getColumn().getId().equals(columnId)) {
            Column currentColumn = task.getColumn();
            currentColumn.getTasks().remove(task);
            Column newColumn = columnRepo.findById(columnId).orElseThrow(() -> new IllegalArgumentException("Column not found"));
            task.setColumn(newColumn);
            newColumn.getTasks().add(task);
            columnRepo.save(currentColumn);
            columnRepo.save(newColumn);
        }

        // Assign user if provided
        if (userId != null) {
            User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            task.getUsers().add(user);
        }

        taskRepo.save(task);

        return "redirect:/tasks/edit/" + id;
    }


    @GetMapping("/removeAttachment/{taskId}/{attachmentId}")
    public String removeAttachment(@PathVariable Integer taskId, @PathVariable Integer attachmentId) {
        Task task = taskRepo.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // Find the attachment to be removed
        Attachment attachmentToRemove = null;
        for (Attachment attachment : task.getAttachments()) {
            if (attachment.getId().equals(attachmentId)) {
                attachmentToRemove = attachment;
                break;
            }
        }

        // Check if the attachment exists
        if (attachmentToRemove != null) {
            // Delete associated records in attachment_content table
            attachmentContentRepo.deleteByAttachment(attachmentToRemove);

            // Remove the attachment from the task's attachments list
            task.getAttachments().remove(attachmentToRemove);

            // Save the updated task
            taskRepo.save(task);
        } else {
            // Handle case where attachment is not found
            // You can redirect to an error page or handle it differently based on your requirements
        }

        return "redirect:/tasks/edit/" + taskId;
    }

    @GetMapping("/removeUser/{taskId}/{userId}")
    public String removeUserFromTask(@PathVariable Integer taskId, @PathVariable Integer userId) {
        Task task = taskRepo.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        task.getUsers().remove(user);
        taskRepo.save(task);
        return "redirect:/tasks/edit/" + taskId;
    }




    @PostMapping("/addComment/{id}")
    public String addComment(@PathVariable(required = false)  Integer id ,
                             @RequestParam String comment) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (id==null){
            return "redirect:/login";
        }

        String username = authentication.getName();

        Task task = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // Create a new comment
        Comment newComment = new Comment();
        newComment.setComment(comment);

        User user = userRepo.findByUsername(username);
        newComment.setUser(user);
        task.getComments().add(newComment);

        taskRepo.save(task);

        return "redirect:/tasks/edit/" + id;
    }

    @PostMapping("/assignUser/{id}")
    public String assignUser(@PathVariable Integer id,
                             @RequestParam Integer userId) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        task.getUsers().add(user);
        taskRepo.save(task);
        return "redirect:/tasks/edit/" + id;
    }

}
