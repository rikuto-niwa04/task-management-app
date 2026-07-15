package com.example.taskmanagementapp.web.controller;

import com.example.taskmanagementapp.domain.task.Task;
import com.example.taskmanagementapp.domain.task.TaskOperation;
import com.example.taskmanagementapp.domain.task.TaskStatus;
import com.example.taskmanagementapp.domain.user.UserRepository;
import com.example.taskmanagementapp.service.TaskService;
import com.example.taskmanagementapp.web.form.TaskCreateForm;
import com.example.taskmanagementapp.web.form.TaskSearchForm;
import com.example.taskmanagementapp.web.form.TaskUpdateForm;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.example.taskmanagementapp.domain.user.User;
import com.example.taskmanagementapp.domain.user.UserRepository;


@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    // Constructor injection of TaskService
    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new TaskCreateForm());
        return "tasks/new";
    }

    // Handle form submission for creating a new task
    @PostMapping
    public String create(@Valid @ModelAttribute("form") TaskCreateForm form,
                         BindingResult bindingResult,
                         Authentication auth) {
        if (bindingResult.hasErrors()) {
            return "tasks/new";
        }
            User loginUser = loginUser(auth);
        taskService.create(form, loginUser.getUsername());
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Task t = taskService.getOrThrow(id);

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle(t.getTitle());
        form.setDescription(t.getDescription());
        form.setDueDate(t.getDueDate());
        form.setAssigneeId(t.getAssigneeId());

        model.addAttribute("task", t);
        model.addAttribute("form", form);
        model.addAttribute("auditLogs", taskService.auditLogs(id));
        return "tasks/edit";
    }

    @GetMapping
    public String list(@ModelAttribute("searchForm") TaskSearchForm searchForm,
                    @RequestParam(defaultValue = "0") int page,
                    @RequestParam(defaultValue = "dueDate,asc") String sort,
                    Model model) {

        Page<Task> taskPage = taskService.search(searchForm, page, 10, sort);

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("sort", sort);

        return "tasks/list";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") TaskUpdateForm form,
                         BindingResult bindingResult,
                         Authentication auth,
                         Model model) {
        if (bindingResult.hasErrors()) {
            Task t = taskService.getOrThrow(id);
            model.addAttribute("task", t);
            model.addAttribute("auditLogs", taskService.auditLogs(id));
            return "tasks/edit";
        }
        User loginUser = loginUser(auth);

        taskService.updateFields(
                id,
                form,
                loginUser.getUsername(),
                loginUser.getId(),
                loginUser.getRole().name()
        );
        return "redirect:/tasks/" + id + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth) {
        User loginUser = loginUser(auth);

        taskService.delete(
                id,
                loginUser.getUsername(),
                loginUser.getId(),
                loginUser.getRole().name()
        );

        return "redirect:/tasks";
    }

    private User loginUser(Authentication auth) {
        if (auth == null) {
            throw new IllegalStateException("Authentication is required.");
        }

        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Login user not found: " + auth.getName()
                        )
                );
    }

    @PostMapping("/{id}/status")
    public String changeStatus(
            @PathVariable Long id,
            @RequestParam TaskOperation operation,
            Authentication auth
    ) {
        User loginUser = loginUser(auth);

        taskService.changeStatus(
                id,
                operation,
                loginUser.getUsername(),
                loginUser.getId(),
                loginUser.getRole().name()
        );

        return "redirect:/tasks";
    }

}

