package com.example.Excermol.repository;

import com.example.Excermol.entity.CampaignLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignLeadRepository  extends JpaRepository<CampaignLead, Long> {
    List<CampaignLead> findByCampaignId(Long campaignId);

}
