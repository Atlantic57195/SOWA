package com.berkdagli.sowa.service;

import com.berkdagli.sowa.dto.NoteDto;
import com.berkdagli.sowa.model.Note;
import com.berkdagli.sowa.model.User;
import com.berkdagli.sowa.repository.NoteRepository;
import com.berkdagli.sowa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    private User testUser;
    private Note testNote;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@example.com", "password", "USER");
        testUser.setId(1L);
        testNote = new Note("Title", "Content", testUser);
        testNote.setId(10L);
    }

    @Test
    void findAllByUserEmail_Success() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(noteRepository.findByUserIdNative(1L)).thenReturn(Collections.singletonList(testNote));

        // Act
        List<Note> notes = noteService.findAllByUserEmail("test@example.com");

        // Assert
        assertNotNull(notes);
        assertFalse(notes.isEmpty());
        assertEquals(1, notes.size());
        assertEquals(testNote, notes.get(0));

        verify(userRepository).findByEmail("test@example.com");
        verify(noteRepository).findByUserIdNative(1L);
    }

    @Test
    void findByIdAndUserEmail_Success() {
        // Arrange
        when(noteRepository.findById(10L)).thenReturn(Optional.of(testNote));

        // Act
        Note note = noteService.findByIdAndUserEmail(10L, "test@example.com");

        // Assert
        assertNotNull(note);
        assertEquals(10L, note.getId());
        assertEquals("Title", note.getTitle());

        verify(noteRepository).findById(10L);
    }

    @Test
    void findByIdAndUserEmail_AccessDenied() {
        // Arrange
        User otherUser = new User("other", "other@example.com", "password", "USER");
        Note otherNote = new Note("Other Title", "Content", otherUser);
        otherNote.setId(20L);

        when(noteRepository.findById(20L)).thenReturn(Optional.of(otherNote));

        // Act & Assert
        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            noteService.findByIdAndUserEmail(20L, "test@example.com");
        });

        assertEquals("You do not have permission to access this note", exception.getMessage());
        verify(noteRepository).findById(20L);
    }

    @Test
    void findByIdAndUserEmail_NotFound() {
        // Arrange
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            noteService.findByIdAndUserEmail(99L, "test@example.com");
        });

        assertEquals("Note not found", exception.getMessage());
        verify(noteRepository).findById(99L);
    }

    @Test
    void createNote_Success() {
        // Arrange
        NoteDto noteDto = new NoteDto(); // Assuming NoteDto has setter or default constructor
        noteDto.setTitle("New Title");
        noteDto.setContent("New Content");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(noteRepository.save(any(Note.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        noteService.createNote(noteDto, "test@example.com");

        // Assert
        verify(userRepository).findByEmail("test@example.com");
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void updateNote_Success() {
        // Arrange
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("Updated Title");
        noteDto.setContent("Updated Content");

        when(noteRepository.findById(10L)).thenReturn(Optional.of(testNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        noteService.updateNote(10L, noteDto, "test@example.com");

        // Assert
        assertEquals("Updated Title", testNote.getTitle());
        assertEquals("Updated Content", testNote.getContent());
        verify(noteRepository).findById(10L);
        verify(noteRepository).save(testNote);
    }

    @Test
    void deleteNote_Success() {
        // Arrange
        when(noteRepository.findById(10L)).thenReturn(Optional.of(testNote));

        // Act
        noteService.deleteNote(10L, "test@example.com");

        // Assert
        verify(noteRepository).findById(10L);
        verify(noteRepository).delete(testNote);
    }

    @Test
    void getAllNotes_Success() {
        // Arrange
        when(noteRepository.findAll()).thenReturn(java.util.Collections.singletonList(testNote));

        // Act
        List<Note> notes = noteService.getAllNotes();

        // Assert
        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals(testNote, notes.get(0));
        verify(noteRepository).findAll();
    }
}
