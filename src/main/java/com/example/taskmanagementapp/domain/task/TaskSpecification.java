package com.example.taskmanagementapp.domain.task;

import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("assigneeId"), assigneeId);
    }
}