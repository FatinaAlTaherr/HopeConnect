package com.HopeConnect.HC.controllers.PartnershipControllers;

import com.HopeConnect.HC.models.Partnership.Partner;
import com.HopeConnect.HC.services.PartnershipServices.PartnerService;
import com.HopeConnect.HC.DTO.PartnerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/HopeConnect/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DONOR', 'VOLUNTEER', 'SPONSOR', 'ORPHANAGE_OWNER')")
    public ResponseEntity<List<PartnerDTO>> getPartnersByType(@PathVariable Partner.PartnerType type) {
        List<PartnerDTO> dtoList = partnerService.getPartnersByType(type)
                .stream()
                .map(partnerService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/status/verified")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DONOR', 'VOLUNTEER', 'SPONSOR', 'ORPHANAGE_OWNER')")
    public ResponseEntity<List<PartnerDTO>> getVerifiedPartners() {
        List<PartnerDTO> dtoList = partnerService.getPartnersByVerificationStatus(true)
                .stream()
                .map(partnerService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/status/pending")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DONOR', 'VOLUNTEER', 'SPONSOR', 'ORPHANAGE_OWNER')")
    public ResponseEntity<List<PartnerDTO>> getPendingPartners() {
        List<PartnerDTO> dtoList = partnerService.getPartnersByVerificationStatus(false)
                .stream()
                .map(partnerService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ORPHANAGE_OWNER')")
    public ResponseEntity<PartnerDTO> createPartner(@RequestBody Partner partner) {
        Partner created = partnerService.createPartner(partner);
        return ResponseEntity.ok(partnerService.mapToDTO(created));
    }

    @GetMapping("/orphanage/{orphanageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DONOR', 'VOLUNTEER', 'SPONSOR', 'ORPHANAGE_OWNER')")
    public ResponseEntity<List<PartnerDTO>> getPartnersByOrphanage(@PathVariable Long orphanageId) {
        List<PartnerDTO> dtoList = partnerService.getPartnersByOrphanage(orphanageId)
                .stream()
                .map(partnerService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ORPHANAGE_OWNER')")
    public ResponseEntity<PartnerDTO> verifyPartner(@PathVariable Long id,
                                                    @RequestParam String documentUrl) {
        Partner verified = partnerService.verifyPartner(id, documentUrl);
        return ResponseEntity.ok(partnerService.mapToDTO(verified));
    }
}
