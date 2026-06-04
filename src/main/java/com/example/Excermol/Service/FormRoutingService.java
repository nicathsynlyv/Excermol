package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.FormRoutingCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormRoutingResponseDTO;
import com.example.Excermol.entity.dtos.FormRoutingUpdateRequestDTO;

import java.util.List;

public interface FormRoutingService  {
    // routing yarat
    FormRoutingResponseDTO createRouting(FormRoutingCreateRequestDTO dto);

    // forma gore butun routingler
    List<FormRoutingResponseDTO> getRoutingsByFormId(Long formId);

    // id-ye gore routing
    FormRoutingResponseDTO getRoutingById(Long id);

    // routing-i update et
    FormRoutingResponseDTO updateRouting(Long id, FormRoutingUpdateRequestDTO dto);

    // routing-i sil
    void deleteRouting(Long id);
}
