package com.vijay.crudApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.crudApi.models.Note;
import com.vijay.crudApi.service.NoteService;

@RestController
@CrossOrigin("http://localhost:3838")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping(value = "/api/notes")
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @PostMapping(value = "/api/notes/save")
    public Note createOrUpdateNote(@RequestBody Note note) {
        return noteService.createOrUpdateNote(note);
    }

    @DeleteMapping("/api/notes/{id}")
    public void deleteNoteById(@PathVariable Long id) {
        noteService.deleteNoteById(id);
    }
}
