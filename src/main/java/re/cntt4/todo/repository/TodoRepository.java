package re.cntt4.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.cntt4.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}