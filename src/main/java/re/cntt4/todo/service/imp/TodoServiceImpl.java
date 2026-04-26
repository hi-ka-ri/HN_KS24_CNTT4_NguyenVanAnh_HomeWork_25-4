package re.cntt4.todo.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.cntt4.todo.entity.Todo;
import re.cntt4.todo.repository.TodoRepository;
import re.cntt4.todo.service.TodoService;

import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository repo;

    @Override
    public List<Todo> findAll() {
        return repo.findAll();
    }

    @Override
    public void save(Todo todo) {
        repo.save(todo);
    }
    @Override
    public Optional<Todo> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}