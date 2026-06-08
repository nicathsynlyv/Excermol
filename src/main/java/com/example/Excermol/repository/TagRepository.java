package com.example.Excermol.repository;

import com.example.Excermol.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // ada görə tag tap
    Optional<Tag> findByName(String name);

    // ada görə axtarış
    List<Tag> findAllByNameContainingIgnoreCase(String name);

    // bu ad artıq varmı?
    boolean existsByName(String name);
}
