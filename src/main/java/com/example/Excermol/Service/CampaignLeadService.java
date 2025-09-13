package com.example.Excermol.Service;

import com.example.Excermol.entity.CampaignLead;

import java.util.List;

public interface CampaignLeadService extends BaseService<CampaignLead,Long> {
    List<CampaignLead> findByCampaignId(Long campaignId);

}
