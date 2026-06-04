package com.example.Excermol.Service;

import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.dtos.FormCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormResponseDTO;
import com.example.Excermol.entity.dtos.FormUpdateRequestDTO;

import java.util.List;

public interface FormService  {

    // form yarat
    FormResponseDTO createForm(FormCreateRequestDTO dto, Long ownerId);

    // butun formlar
    List<FormResponseDTO> getAllForms();

    // id-ye gore form
    FormResponseDTO getFormById(Long id);

    // owner-a gore formlar
    List<FormResponseDTO> getFormsByOwner(Long ownerId);

    // formu update et
    FormResponseDTO updateForm(Long id, FormUpdateRequestDTO dto);

    // formu sil
    void deleteForm(Long id);

    // formu publish et / draft-a qaytar
    FormResponseDTO toggleFormStatus(Long id);
}
