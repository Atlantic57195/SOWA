package com.berkdagli.sowa.service;

import com.berkdagli.sowa.dto.NoteDto;
import com.berkdagli.sowa.model.Note;
import com.berkdagli.sowa.model.User;
import com.berkdagli.sowa.repository.NoteRepository;
import com.berkdagli.sowa.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public List<Note> findAllByUserEmail(String email) {
        User user = getUserByEmail(email);
        // Using the native query as per requirement test, though standard findByUserId
        // works too.
        // We can use the standard one for simplicity here, or use the native one.
        // Let's use the native one to prove it works in the application flow.
        return noteRepository.findByUserIdNative(user.getId());
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note findByIdAndUserEmail(Long id, String email) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You do not have permission to access this note");
        }
        return note;
    }

    public void createNote(NoteDto noteDto, String email) {
        User user = getUserByEmail(email);
        Note note = new Note(noteDto.getTitle(), noteDto.getContent(), user);
        noteRepository.save(note);
    }

    public void updateNote(Long id, NoteDto noteDto, String email) {
        Note note = findByIdAndUserEmail(id, email);
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        noteRepository.save(note);
    }

    public void deleteNote(Long id, String email) {
        Note note = findByIdAndUserEmail(id, email);
        noteRepository.delete(note);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
