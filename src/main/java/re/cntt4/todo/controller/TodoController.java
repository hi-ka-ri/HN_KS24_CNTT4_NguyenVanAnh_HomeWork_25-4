package re.cntt4.todo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import re.cntt4.todo.dto.TodoDTO;
import re.cntt4.todo.entity.Priority;
import re.cntt4.todo.entity.Status;
import re.cntt4.todo.entity.Todo;
import re.cntt4.todo.service.TodoService;

import java.util.Optional;

@Controller
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("todos", service.findAll());
        return "todo-list";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("todoDTO", new TodoDTO());
        model.addAttribute("priorities", Priority.values());
        return "todo-form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("todoDTO") TodoDTO dto,
                      BindingResult result,
                      Model model,
                      RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            return "todo-form";
        }

        Todo todo = new Todo();
        todo.setContent(dto.getContent());
        todo.setDueDate(dto.getDueDate());
        todo.setPriority(dto.getPriority());
        todo.setStatus(Status.PENDING);

        service.save(todo);

        redirectAttributes.addFlashAttribute("message", "Thêm thành công!");
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        Optional<Todo> optional = service.findById(id);
        if (optional.isEmpty()) return "redirect:/";

        Todo t = optional.get();

        TodoDTO dto = new TodoDTO();
        dto.setId(t.getId());
        dto.setContent(t.getContent());
        dto.setDueDate(t.getDueDate());
        dto.setPriority(t.getPriority());

        model.addAttribute("todoDTO", dto);
        model.addAttribute("priorities", Priority.values());

        return "todo-form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("todoDTO") TodoDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            return "todo-form";
        }

        Todo old = service.findById(dto.getId()).orElseThrow();

        old.setContent(dto.getContent());
        old.setDueDate(dto.getDueDate());
        old.setPriority(dto.getPriority());

        service.save(old);

        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        service.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công!");

        return "redirect:/";
    }
}