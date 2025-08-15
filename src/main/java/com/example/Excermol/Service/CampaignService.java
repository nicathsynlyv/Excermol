package com.example.Excermol.Service;

import com.example.Excermol.entity.Campaign;
import com.example.Excermol.enums.CampaignStatus;
import com.example.Excermol.repository.CampaignRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public Campaign create(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    public Campaign update(Long id, Campaign updatedCampaign) {
        Campaign existing = getById(id);
        existing.setCampaignName(updatedCampaign.getCampaignName());
        existing.setStatus(updatedCampaign.getStatus());
        existing.setStartDate(updatedCampaign.getStartDate());
        existing.setEndDate(updatedCampaign.getEndDate());
//        existing.setCompany(updatedCampaign.getCompany());
        return campaignRepository.save(existing);
    }

    public void delete(Long id) {
        campaignRepository.deleteById(id);
    }

    public Campaign getById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));
    }

    public List<Campaign> getAll(String sortBy) {
        return campaignRepository.findAll(Sort.by(sortBy).ascending());
    }

    public List<Campaign> getByStatus(CampaignStatus status) {
        return campaignRepository.findByStatus(status);
    }

    public List<Campaign> searchByName(String keyword) {
        return campaignRepository.findByCampaignNameContainingIgnoreCase(keyword);
    }
}
