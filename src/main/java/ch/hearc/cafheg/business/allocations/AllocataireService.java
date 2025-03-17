package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;

import java.util.List;
import java.util.stream.Collectors;

public class AllocataireService {

    private final AllocataireMapper allocataireMapper;
    private final VersementMapper versementMapper;

    public AllocataireService(AllocataireMapper allocataireMapper, VersementMapper versementMapper) {
        this.allocataireMapper = allocataireMapper;
        this.versementMapper = versementMapper;
    }

    public List<Allocataire> findAllAllocataires(String nom) {
        return allocataireMapper.findAll(nom);
    }

    public boolean supprimerAllocataire(long allocataireId) {
        // Vérifier si l'allocataire possède des versements
        boolean aDesVersements = versementMapper.findVersementParentEnfant().stream()
                .anyMatch(v -> v.getParentId() == allocataireId);


        if (aDesVersements) {
            System.out.println("Impossible de supprimer l'allocataire " + allocataireId + " car il possède des versements.");
            return false;
        }

        // Supprimer l'allocataire
        allocataireMapper.deleteById(allocataireId);
        System.out.println("Allocataire " + allocataireId + " supprimé avec succès.");
        return true;
    }
}
