package com.example.taskmanagementapp.web.form;

import com.example.taskmanagementapp.domain.task.TaskStatus;

public class TaskSearchForm {

    private String title;

    private TaskStatus status;

    private Long assigneeId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
}