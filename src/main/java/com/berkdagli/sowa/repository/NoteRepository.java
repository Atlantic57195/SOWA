package com.berkdagli.sowa.repository;

import com.berkdagli.sowa.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // Standard JPA method
    List<Note> findByUserId(Long userId);

    // Requirement: Use prepared statements for at least one query.
    // Native query with named parameters (which uses prepared statements under the
    // hood in Hibernate/JPA)
    @Query(value = "SELECT * FROM notes WHERE user_id = :userId", nativeQuery = true)
    List<Note> findByUserIdNative(@Param("userId") Long userId);
}
