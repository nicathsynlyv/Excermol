package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.PersonActivityRequestDTO;
import com.example.Excermol.entity.dtos.PersonActivityResponseDTO;

import java.util.List;

public interface PersonActivityService {
    // Activity əlavə et — service-dən çağırılır
    PersonActivityResponseDTO addActivity(PersonActivityRequestDTO requestDTO);

    // Şəxsə aid bütün aktivlikləri gətir — UI-da Activity tab
    List<PersonActivityResponseDTO> getActivitiesByPersonId(Long personId);

    // Activity sil
    void deleteActivity(Long activityId);
}
