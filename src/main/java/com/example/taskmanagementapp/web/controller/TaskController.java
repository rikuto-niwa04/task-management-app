package com.example.taskmanagementapp.web.controller;

import com.example.taskmanagementapp.domain.task.Task;
import com.example.taskmanagementapp.domain.task.TaskOperation;
import com.example.taskmanagementapp.service.TaskService;
import com.example.taskmanagementapp.web.form.TaskCreateForm;
import com.example.taskmanagementapp.web.form.TaskUpdateForm;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new TaskCreateForm());
        return "tasks/new";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") TaskCreateForm form,
                         BindingResult bindingResult,
                         Authentication auth) {
        if (bindingResult.hasErrors()) {
            return "tasks/new";
        }
        taskService.create(form, actor(auth));
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
        taskService.updateFields(id, form, actor(auth), loginUserId(auth), role(auth));
        return "redirect:/tasks/" + id + "/edit";
    }

    @PostMapping("/{id}/operate")
    public String operate(@PathVariable Long id,
                          @RequestParam("op") TaskOperation op,
                          Authentication auth) {
        taskService.operate(id, op, actor(auth), loginUserId(auth), role(auth));
        return "redirect:/tasks/" + id + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth) {
        taskService.delete(id, actor(auth), loginUserId(auth), role(auth));
        return "redirect:/tasks";
    }

    private String actor(Authentication auth) {
        return auth != null ? auth.getName() : "anonymous";
    }

    private Long loginUserId(Authentication auth) {
        return 1L;
    }

    private String role(Authentication auth) {
        return "ADMIN";
    }
}
