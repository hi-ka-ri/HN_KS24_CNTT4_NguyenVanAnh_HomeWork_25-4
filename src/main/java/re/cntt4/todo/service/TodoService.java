package re.cntt4.todo.service;

import re.cntt4.todo.entity.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<Todo> findAll();
    void save(Todo todo);
    Optional<Todo> findById(Long id);
    void deleteById(Long id);
}