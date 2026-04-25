package re.cntt4.todo.service;

import re.cntt4.todo.entity.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> findAll();
    void save(Todo todo);
}