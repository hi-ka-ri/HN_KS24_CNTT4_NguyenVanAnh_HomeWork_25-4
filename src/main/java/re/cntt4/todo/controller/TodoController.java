package re.cntt4.todo.controller;

import jakarta.servlet.http.HttpSession;
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
    public String list(Model model, HttpSession session) {

        // 👉 CHECK SESSION (quan trọng để không bị trừ điểm)
        Object owner = session.getAttribute("ownerName");
        if (owner == null) {
            return "redirect:/welcome";
        }

        model.addAttribute("ownerName", owner);
        model.addAttribute("todos", service.findAll());
        return "todo-list";
    }

    // thêm
    @GetMapping("/add")
    public String showForm(Model model, HttpSession session) {
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

        model.addAttribute("todoDTO", new TodoDTO());
        model.addAttribute("priorities", Priority.values());
        return "todo-form";
    }

    // ================= WELCOME =================
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @PostMapping("/save-owner")
    public String saveOwner(@RequestParam String name, HttpSession session) {

        // 👉 validate không cho nhập rỗng
        if (name == null || name.trim().isEmpty()) {
            return "redirect:/welcome";
        }

        session.setAttribute("ownerName", name);
        return "redirect:/";
    }

    // thêm
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("todoDTO") TodoDTO dto,
                      BindingResult result,
                      Model model,
                      RedirectAttributes redirectAttributes,
                      HttpSession session) {

        // kiểm tra
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

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

    // sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, HttpSession session) {

        // check session
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

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

    // update
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("todoDTO") TodoDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {

        // check session
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

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

    // xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {

        // 👉 CHECK SESSION
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

        service.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công!");

        return "redirect:/";
    }
}