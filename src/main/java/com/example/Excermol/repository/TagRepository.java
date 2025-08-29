package com.example.Excermol.repository;

import com.example.Excermol.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // Məsələn: adı ilə tag tapmaq
    Tag findByName(String name);
}
