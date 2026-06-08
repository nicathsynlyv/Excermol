package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagResponseDTO {
    private Long id;
    private String name;
    private String color;
    private Integer personsCount; // neçə person-da istifadə olunur

}
