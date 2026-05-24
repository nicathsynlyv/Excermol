package com.example.Excermol.Service;

import com.example.Excermol.entity.Tag;

public interface TagService {
    // Məsələn: adı ilə tag tapmaq
    Tag findByName(String name);
}
