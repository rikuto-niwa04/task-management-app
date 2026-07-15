package com.example.taskmanagementapp.web.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(
            IllegalStateException exception,
            Model model
    ) {
        model.addAttribute("message", exception.getMessage());
        return "error/403";
    }
}