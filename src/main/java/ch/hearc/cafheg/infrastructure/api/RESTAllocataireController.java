package ch.hearc.cafheg.infrastructure.api;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.service.AllocataireService;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class RESTAllocataireController {

    private AllocataireService allocataireService;

    public RESTAllocataireController() {
        this.allocataireService = new AllocataireService(new AllocataireMapper(), new VersementMapper());
    }

    @GetMapping
    public List<Allocataire> getAllocataires(@RequestParam(value = "startsWith", required = false) String start) {
        return inTransaction(() -> allocataireService.findAllAllocataires(start));
    }

    @GetMapping("/{noAVS}")
    public ResponseEntity<Allocataire> getAllocataireByNoAVS(@PathVariable String noAVS) {
        return inTransaction(() -> {
            Allocataire allocataire = allocataireService.findByNoAVS(noAVS);
            return allocataire != null ? ResponseEntity.ok(allocataire) : ResponseEntity.notFound().build();
        });
    }

    @PutMapping("/{noAVS}")
    public ResponseEntity<String> updateAllocataire(
            @PathVariable String noAVS,
            @RequestParam String newName,
            @RequestParam String newSurname) {
        return inTransaction(() -> {
            boolean updated = allocataireService.updateAllocataire(noAVS, newName, newSurname);
            return updated
                    ? ResponseEntity.ok("Allocataire updated successfully.")
                    : ResponseEntity.badRequest().body("No changes were made.");
        });
    }

    @DeleteMapping("/{allocataireId}")
    public ResponseEntity<String> deleteAllocataire(@PathVariable long allocataireId) {
        return inTransaction(() -> {
            boolean deleted = allocataireService.deleteAllocataire(allocataireId);
            return deleted
                    ? ResponseEntity.ok("Allocataire deleted successfully.")
                    : ResponseEntity.badRequest().body("Allocataire cannot be deleted due to existing payments.");
        });
    }
}
