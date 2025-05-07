package com.HopeConnect.HC.controllers.OrphanageManagementControllers;

import com.HopeConnect.HC.models.OrphanManagement.Orphan;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.services.OrphanManagementServices.OrphanageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/HopeConnect/api")
@RequiredArgsConstructor
public class OrphanageController {

    private final OrphanageService orphanageService;

    // Orphanages
    @PostMapping("/orphanages")
    public ResponseEntity<Orphanage> addOrphanage(@RequestBody Orphanage orphanage) {
        return ResponseEntity.ok(orphanageService.saveOrphanage(orphanage));
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
        return ResponseEntity.ok(orphanageService.saveOrphan(orphan));
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
