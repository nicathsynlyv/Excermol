package com.example.Excermol.Service;

import com.example.Excermol.entity.Tag;

public interface TagService extends BaseService<Tag,Long>{
    // Məsələn: adı ilə tag tapmaq
    Tag findByName(String name);
}
