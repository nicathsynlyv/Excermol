package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagCreateRequestDTO {

    private String name;   // "Agency", "Scale Up"
    private String color;  // "#FF0000"
}

