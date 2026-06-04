package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.FormSubmitRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitResponseDTO;

import java.util.List;

public interface FormResponseService  {
    // form submit et
    FormSubmitResponseDTO submitForm(FormSubmitRequestDTO dto);

    // forma gore butun cavablar
    List<FormSubmitResponseDTO> getResponsesByFormId(Long formId);

    // id-ye gore cavab
    FormSubmitResponseDTO getResponseById(Long id);

    // cavabi sil
    void deleteResponse(Long id);
}
