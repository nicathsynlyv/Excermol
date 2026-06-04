package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.FormFieldCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormFieldResponseDTO;
import com.example.Excermol.entity.dtos.FormFieldUpdateRequestDTO;

import java.util.List;

public interface FormFieldService  {
    // field yarat
    FormFieldResponseDTO createField(FormFieldCreateRequestDTO dto);

    // forma gore butun fieldler
    List<FormFieldResponseDTO> getFieldsByFormId(Long formId);

    // id-ye gore field
    FormFieldResponseDTO getFieldById(Long id);

    // field-i update et
    FormFieldResponseDTO updateField(Long id, FormFieldUpdateRequestDTO dto);

    // field-i sil
    void deleteField(Long id);
}
