package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.PersonRequestDTO;
import com.example.Excermol.entity.dtos.PersonResponseDTO;

import java.util.List;

public interface PersonService  {

    PersonResponseDTO createPerson(PersonRequestDTO requestDTO);

    List<PersonResponseDTO> getAllPersons();

    PersonResponseDTO getPersonById(Long id);

    PersonResponseDTO updatePerson(Long id, PersonRequestDTO requestDTO);

    void deletePerson(Long id);

}
