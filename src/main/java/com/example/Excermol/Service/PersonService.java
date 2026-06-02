package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.PersonRequestDTO;
import com.example.Excermol.entity.dtos.PersonResponseDTO;
import com.example.Excermol.enums.PersonStatus;

import java.util.List;

public interface PersonService  {

    PersonResponseDTO createPerson(PersonRequestDTO requestDTO);

    List<PersonResponseDTO> getAllPersons();

    PersonResponseDTO getPersonById(Long id);

    PersonResponseDTO updatePerson(Long id, PersonRequestDTO requestDTO);

    void deletePerson(Long id);



    // User ← new changes
    List<PersonResponseDTO> getPersonsByUser(Long userId);

    List<PersonResponseDTO> getPersonsByUserAndStatus(Long userId, PersonStatus status);
}
