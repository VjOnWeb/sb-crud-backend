package com.vijay.crudApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijay.crudApi.Repo.NoteRepository;
import com.vijay.crudApi.models.Note;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @SuppressWarnings("null")
    public Note createOrUpdateNote(Note note) {
        return noteRepository.save(note);
    }

    @SuppressWarnings("null")

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }
}
