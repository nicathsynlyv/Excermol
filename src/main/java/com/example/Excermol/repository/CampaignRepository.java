package com.example.Excermol.repository;

import com.example.Excermol.entity.Campaign;
import com.example.Excermol.enums.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> , JpaSpecificationExecutor<Campaign> {
    //    JpaSpecificationExecutor bizə çoxlu filterləri eyni anda tətbiq etməyə imkan verir
    // Status-a görə filter
    List<Campaign> findByStatus(CampaignStatus status);

    // Şirkətə görə filter
//    List<Campaign> findByCompanyId(Long companyId);

    // Assignee-yə görə filter
//    List<Campaign> findByAssignedToId(Long userId);

    // Ada görə axtarış
    List<Campaign> findByCampaignNameContainingIgnoreCase(String name);
}
