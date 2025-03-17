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
        return allocataireMapper.findAll(nom) ;
    }

    public boolean updateAllocataire(String noAVS, String nouveauNom, String nouveauPrenom) {
        // Récupérer l'allocataire existant
        Allocataire allocataire = allocataireMapper.findByNoAVS(noAVS);
        if (allocataire == null) {
            throw new IllegalArgumentException("Allocataire non trouvé avec le numéro AVS : " + noAVS);
        }

        // Vérifier si des changements ont été effectués
        if (allocataire.getNom().equals(nouveauNom) && allocataire.getPrenom().equals(nouveauPrenom)) {
            return false; // Aucun changement nécessaire
        }

        // Modifier l'allocataire
        allocataire.setNom(nouveauNom);
        allocataire.setPrenom(nouveauPrenom);
        allocataireMapper.updateAllocataire(allocataire.getNoAVS().getValue(), allocataire.getNom(), allocataire.getPrenom());

        return true; // Modification effectuée
    }

    public boolean deleteAllocataire(long allocataireId) {
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

    public Allocataire findByNoAVS(String noAVS) {
        return allocataireMapper.findByNoAVS(noAVS);
    }
}
