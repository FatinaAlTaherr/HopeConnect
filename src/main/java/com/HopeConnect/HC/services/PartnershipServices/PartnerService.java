package com.HopeConnect.HC.services.PartnershipServices;

import com.HopeConnect.HC.DTO.OrphanageSummaryDTO;
import com.HopeConnect.HC.DTO.PartnerDTO;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.Partnership.Partner;
import com.HopeConnect.HC.repositories.OrphanManagementRepositories.OrphanageRepository;
import com.HopeConnect.HC.repositories.PartnershipRepositories.PartnerRepository;
import com.HopeConnect.HC.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final OrphanageRepository orphanageRepository;

    private final EmailSenderService emailSenderService;  // ✅ Inject Email Service

    public PartnerDTO mapToDTO(Partner partner) {
        OrphanageSummaryDTO orphanageDTO = null;
        if (partner.getSupportedOrphanage() != null) {
            var o = partner.getSupportedOrphanage();
            orphanageDTO = new OrphanageSummaryDTO(
                    o.getId(),
                    o.getName(),
                    o.getEmail(),
                    o.getLocation()
            );
        }

        return new PartnerDTO(
                partner.getId(),
                partner.getName(),
                partner.getContactEmail(),
                partner.getPhone(),
                partner.getType(),
                orphanageDTO,
                partner.getPartnershipStartDate(),
                partner.getUpdatedAt(),
                partner.isVerified(),
                partner.getVerificationDocumentsUrl()
        );
    }

    public Partner createPartner(Partner partner) {
        if (partner.getSupportedOrphanage() != null && partner.getSupportedOrphanage().getId() != null) {
            Orphanage orphanage = orphanageRepository.findById(partner.getSupportedOrphanage().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Orphanage not found"));
            partner.setSupportedOrphanage(orphanage);
        }

        Partner savedPartner = partnerRepository.save(partner);

        // ✅ Send email notification
        String subject = "Complete Your Partnership with HopeConnect";
        String message = String.format(
                "Dear %s,\n\nThank you for registering as a partner with HopeConnect.\n" +
                        "To complete the partnership, please send all necessary official documents to hope@org.com.\n\n" +
                        "Best regards,\nHopeConnect Team",
                partner.getName()
        );

        emailSenderService.sendEmail(
                partner.getContactEmail(),
                subject,
                message
        );

        return savedPartner;
    }

    // Get partners by orphanage ID
    public List<Partner> getPartnersByOrphanage(Long orphanageId) {
        return partnerRepository.findBySupportedOrphanageId(orphanageId);
    }

    // Verify a partner (admin-only)
    public Partner verifyPartner(Long partnerId, String documentUrl) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("Partner not found"));
        partner.setVerified(true);
        partner.setVerificationDocumentsUrl(documentUrl);
        return partnerRepository.save(partner);
    }

    // Get partners by type (NGO, Charity, etc.)
    public List<Partner> getPartnersByType(Partner.PartnerType type) {
        return partnerRepository.findByType(type);
    }

    // Get all verified/unverified partners
    public List<Partner> getPartnersByVerificationStatus(boolean verified) {
        return partnerRepository.findByVerified(verified);
    }
}