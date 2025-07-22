package com.vijay.crudApi.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.vijay.crudApi.models.Note;
import com.vijay.crudApi.service.NoteService;
import com.vijay.crudApi.Repo.ErrorLogService;

@RestController
//@CrossOrigin("http://localhost:3838")
@CrossOrigin(origins = "${cors.allowed-origin}")

public class NoteController {

    private static final Logger log = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteService noteService;

    @Autowired
    private ErrorLogService errorLogService;

    @GetMapping(value = "/api/notes")
    public List<Note> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();

        log.info("Fetched all notes, count={}", notes.size());
        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            "NoteController",
            "FETCH_NOTES",
            "Fetched all notes",
            Map.of("Count", String.valueOf(notes.size()))
        );

        return notes;
    }

    @PostMapping(value = "/api/notes/save")
    public Note createOrUpdateNote(@RequestBody Note note) {
        Note savedNote = noteService.createOrUpdateNote(note);

        log.info("Saved note with ID {}", savedNote.getId());
        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            "NoteController",
            "SAVE_NOTE",
            "Saved or updated note",
            Map.of("ID", String.valueOf(savedNote.getId()))
        );

        return savedNote;
    }

    @DeleteMapping("/api/notes/{id}")
    public void deleteNoteById(@PathVariable Long id) {
        noteService.deleteNoteById(id);

        log.info("Deleted note with ID {}", id);
        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            "NoteController",
            "DELETE_NOTE",
            "Deleted note",
            Map.of("ID", String.valueOf(id))
        );
    }
}
