package com.berkdagli.sowa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.berkdagli.sowa.service.NoteService;
import org.springframework.ui.Model;

@Controller
public class HelloController {

    private final NoteService noteService;

    public HelloController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("notes", noteService.getAllNotes());
        return "admin";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
