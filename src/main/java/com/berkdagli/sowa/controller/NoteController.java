package com.berkdagli.sowa.controller;

import com.berkdagli.sowa.dto.NoteDto;
import com.berkdagli.sowa.model.Note;
import com.berkdagli.sowa.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public String listNotes(Model model, Principal principal) {
        List<Note> notes = noteService.findAllByUserEmail(principal.getName());
        model.addAttribute("notes", notes);
        return "notes/list"; // Thymeleaf template
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("note")) {
            model.addAttribute("note", new NoteDto());
        }
        return "notes/form";
    }

    @PostMapping
    public String createNote(@Valid @ModelAttribute("note") NoteDto noteDto,
            BindingResult result,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "notes/form";
        }
        noteService.createNote(noteDto, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Note created successfully!");
        return "redirect:/notes";
    }

    @GetMapping({"/edit", "/edit/"})
    public String handleMissingId(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "You must type valid ID.");
        return "redirect:/notes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (id == null || id < 0) {
            redirectAttributes.addFlashAttribute("errorMessage","Note ID cannot be null or less than 0.");
            return "redirect:/notes";
        }
        try {
            Note note = noteService.findByIdAndUserEmail(id, principal.getName());
            NoteDto noteDto = new NoteDto();
            noteDto.setTitle(note.getTitle());
            noteDto.setContent(note.getContent());
            model.addAttribute("note", noteDto);
            model.addAttribute("noteId", id); // To distinguish from create in the form
            return "notes/form";
        } catch (RuntimeException e) {
            return "redirect:/access-denied"; // Or handle more gracefully
        }
    }

    @PostMapping("/update/{id}")
    public String updateNote(@PathVariable Long id,
            @Valid @ModelAttribute("note") NoteDto noteDto,
            BindingResult result,
            Principal principal,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("noteId", id);
            return "notes/form";
        }
        try {
            noteService.updateNote(id, noteDto, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Note updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating note.");
        }
        return "redirect:/notes";
    }

    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Note deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting note.");
        }
        return "redirect:/notes";
    }
}
