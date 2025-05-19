package com.HopeConnect.HC.controllers.OrphanageManagementControllers;

import com.HopeConnect.HC.models.OrphanManagement.Orphan;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.services.ExternalAPI.ChatbotService;
import com.HopeConnect.HC.services.OrphanManagementServices.OrphanageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/HopeConnect/api")
@RequiredArgsConstructor
public class OrphanageController {

    private final OrphanageService orphanageService;

    private final ChatbotService chatbotService;

    @GetMapping("/generate-report/{orphanId}")
    public ResponseEntity<String> generateOrphanReport(@PathVariable Long orphanId) {
        Orphan orphan = orphanageService.getOrphanById(orphanId);
        if (orphan == null) {
            return ResponseEntity.notFound().build();
        }

        String report = chatbotService.generateOrphanReport(orphan);
        return ResponseEntity.ok(report);
    }
    // Orphanages
    @PostMapping("/orphanages")

    public ResponseEntity<Orphanage> addOrphanage(@RequestBody Orphanage orphanage) {
        // Extract logged-in user email from Security Context (from token)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Orphanage savedOrphanage = orphanageService.saveOrphanageWithOwner(orphanage, currentUserEmail);
        return ResponseEntity.ok(savedOrphanage);
    }

    @GetMapping("/orphanages/{id}/orphans")
    public ResponseEntity<List<Orphan>> getOrphansByOrphanageId(@PathVariable Long id) {
        List<Orphan> orphans = orphanageService.getOrphansByOrphanageId(id);
        return ResponseEntity.ok(orphans);
    }
    @GetMapping("/orphanages")
    public ResponseEntity<List<Orphanage>> getAllOrphanages() {
        return ResponseEntity.ok(orphanageService.getAllOrphanages());
    }

    @GetMapping("/orphanages/{id}")
    public ResponseEntity<Orphanage> getOrphanage(@PathVariable Long id) {
        return ResponseEntity.ok(orphanageService.getOrphanageById(id));
    }

    // Orphanage Verification
    @PutMapping("/orphanages/verify/{id}")
    public ResponseEntity<Orphanage> verifyOrphanage(@PathVariable Long id) {
        Orphanage orphanage = orphanageService.verifyOrphanage(id);
        if (orphanage != null) {
            return ResponseEntity.ok(orphanage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Orphanage Deletion
    @DeleteMapping("/orphanages/{id}")
    public ResponseEntity<?> deleteOrphanage(@PathVariable Long id) {
        orphanageService.deleteOrphanage(id);
        return ResponseEntity.noContent().build();
    }

    // Orphans
    @PostMapping("/orphans")
    public ResponseEntity<Orphan> addOrphan(@RequestBody Orphan orphan) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerEmail = authentication.getName();

        Orphan savedOrphan = orphanageService.saveOrphanForOwner(orphan, ownerEmail);
        return ResponseEntity.ok(savedOrphan);
    }

    @GetMapping("/orphans")
    public ResponseEntity<List<Orphan>> getAllOrphans() {
        return ResponseEntity.ok(orphanageService.getAllOrphans());
    }

    @GetMapping("/orphans/{id}")
    public ResponseEntity<Orphan> getOrphan(@PathVariable Long id) {
        return ResponseEntity.ok(orphanageService.getOrphanById(id));
    }

    @PutMapping("/orphans/{id}")
    public ResponseEntity<Orphan> updateOrphan(@PathVariable Long id, @RequestBody Orphan orphan) {
        return ResponseEntity.ok(orphanageService.updateOrphan(id, orphan));
    }

    @DeleteMapping("/orphans/{id}")
    public ResponseEntity<?> deleteOrphan(@PathVariable Long id) {
        orphanageService.deleteOrphan(id);
        return ResponseEntity.noContent().build();
    }
}
