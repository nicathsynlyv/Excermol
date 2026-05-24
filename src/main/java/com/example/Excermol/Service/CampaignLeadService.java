package com.example.Excermol.Service;

import com.example.Excermol.entity.CampaignLead;

import java.util.List;

public interface CampaignLeadService  {
    List<CampaignLead> findByCampaignId(Long campaignId);

}
