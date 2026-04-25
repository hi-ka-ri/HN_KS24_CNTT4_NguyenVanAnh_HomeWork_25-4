package re.cntt4.todo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import re.cntt4.todo.dto.TodoDTO;
import re.cntt4.todo.entity.Priority;
import re.cntt4.todo.entity.Status;
import re.cntt4.todo.entity.Todo;
import re.cntt4.todo.service.TodoService;

@Controller
public class TodoController {

    @Autowired
    private TodoService service;

    // Trang danh sách
    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("todos", service.findAll());
        return "todo-list";
    }

    // Form thêm
    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("todoDTO", new TodoDTO());
        return "todo-form";
    }

    //  Xử lý thêm
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("todoDTO") TodoDTO dto,
                      BindingResult result) {

        if (result.hasErrors()) {
            return "todo-form";
        }

        Todo todo = new Todo();
        todo.setContent(dto.getContent());
        todo.setDueDate(dto.getDueDate());
        todo.setStatus(Status.PENDING);
        todo.setPriority(Priority.MEDIUM);
        service.save(todo);

        service.save(todo);

        return "redirect:/";
    }
}