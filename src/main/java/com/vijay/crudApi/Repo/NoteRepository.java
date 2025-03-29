package com.vijay.crudApi.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.crudApi.models.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
