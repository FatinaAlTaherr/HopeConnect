package com.HopeConnect.HC.controllers.SponsorshipControllers;

import com.HopeConnect.HC.DTO.SponsorshipRequest;
import com.HopeConnect.HC.models.Sponsorship.Sponsorship;
import com.HopeConnect.HC.services.SponsershipServices.SponsorshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("HopeConnect/api/sponsorships")
@RequiredArgsConstructor
public class SponsorshipController {

    private final SponsorshipService sponsorshipService;

    @PreAuthorize("hasAuthority('ORPHANAGE_OWNER')")
    @PutMapping("/{id}/accept")
    public void acceptSponsorship(@PathVariable Long id) {
        sponsorshipService.acceptSponsorship(id);
    }

    @PreAuthorize("hasAuthority('SPONSOR')")
    @PostMapping
    public Sponsorship create(@RequestBody SponsorshipRequest request, Authentication authentication) {
        String userEmail = authentication.getName();
        request.setUserEmail(userEmail);
        return sponsorshipService.createSponsorship(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DONOR', 'VOLUNTEER', 'SPONSOR', 'ORPHANAGE_OWNER')")
    @GetMapping
    public List<Sponsorship> getAll() {
        return sponsorshipService.getAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DONOR', 'VOLUNTEER', 'SPONSOR', 'ORPHANAGE_OWNER')")
    @GetMapping("/user/{email}")
    public List<Sponsorship> getByUser(@PathVariable String email) {
        return sponsorshipService.getByUser(email);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DONOR', 'VOLUNTEER', 'SPONSOR', 'ORPHANAGE_OWNER')")
    @GetMapping("/orphan/{orphanId}")
    public List<Sponsorship> getByOrphan(@PathVariable Long orphanId) {
        return sponsorshipService.getByOrphan(orphanId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ORPHANAGE_OWNER')")
    @PutMapping("/{id}/end")
    public void endSponsorship(@PathVariable Long id) {
        sponsorshipService.endSponsorship(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteSponsorship(@PathVariable Long id) {
        sponsorshipService.deleteSponsorship(id);
    }

}
