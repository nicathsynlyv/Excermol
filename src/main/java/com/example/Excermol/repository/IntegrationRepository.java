package com.example.Excermol.repository;

import com.example.Excermol.entity.Integration;
import com.example.Excermol.enums.IntegrationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationRepository extends JpaRepository<Integration, Long> {

    // workspace e gore butun integration tapmaq
    List<Integration> findByWorkspaceId(Long workspaceId);

    // workspace e gore aktiv integrationlar
    List<Integration> findByWorkspaceIdAndIsActive(Long workspaceId, Boolean isActive);

    // workkspace ve tipe gore integration tapmaq
    Optional<Integration> findByWorkspaceIdAndIntegrationType(Long workspaceId, IntegrationType integrationType);


}
