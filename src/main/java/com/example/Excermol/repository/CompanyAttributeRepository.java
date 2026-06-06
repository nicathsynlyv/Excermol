package com.example.Excermol.repository;

import com.example.Excermol.entity.CompanyAttribute;
import com.example.Excermol.enums.AttributeProperty;
import com.example.Excermol.enums.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyAttributeRepository extends JpaRepository<CompanyAttribute, Long> {

    // workspace-ə görə butun atributlar
    List<CompanyAttribute> findAllByWorkspaceId(Long workspaceId);

    // workspace e gore system atributlari
    List<CompanyAttribute> findAllByWorkspaceIdAndAttributeProperty(Long workspaceId, AttributeProperty attributeProperty);

    //workspace gore tipe gore atributlar
    List<CompanyAttribute> findAllByWorkspaceIdAndAttributeType(Long workspaceId, AttributeType attributeType);

    // ada gore search etmek
    List<CompanyAttribute> findAllByWorkspaceIdAndNameContainingIgnoreCase(Long workspaceId, String name);


}
