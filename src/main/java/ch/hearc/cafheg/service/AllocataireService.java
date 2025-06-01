package ch.hearc.cafheg.service;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class AllocataireService {

    private static final Logger logger = LoggerFactory.getLogger(AllocataireService.class);

    private final AllocataireMapper allocataireMapper;
    private final VersementMapper versementMapper;
    private final Connection connection;

    public AllocataireService(AllocataireMapper allocataireMapper, VersementMapper versementMapper) {
        this.allocataireMapper = allocataireMapper;
        this.versementMapper = versementMapper;
        this.connection = null;
    }

    public AllocataireService(Connection connection) {
        this.allocataireMapper = new AllocataireMapper();
        this.versementMapper = new VersementMapper();
        this.connection = connection;
    }

    public List<Allocataire> findAllAllocataires(String nom) {
        return allocataireMapper.findAll(nom);
    }

    public boolean updateAllocataire(String noAVS, String nouveauNom, String nouveauPrenom) {
        Allocataire allocataire = allocataireMapper.findByNoAVS(noAVS);
        if (allocataire == null) {
            throw new IllegalArgumentException("Allocataire non trouvé avec le numéro AVS : " + noAVS);
        }

        if (allocataire.getNom().equals(nouveauNom) && allocataire.getPrenom().equals(nouveauPrenom)) {
            return false;
        }

        allocataire.setNom(nouveauNom);
        allocataire.setPrenom(nouveauPrenom);
        allocataireMapper.updateAllocataire(allocataire.getNoAVS().getValue(), allocataire.getNom(), allocataire.getPrenom());

        return true;
    }

    public boolean deleteAllocataire(long allocataireId) {
        boolean aDesVersements = versementMapper.findVersementParentEnfant().stream()
                .anyMatch(v -> v.getParentId() == allocataireId);

        if (aDesVersements) {
            logger.info("Impossible de supprimer l'allocataire " + allocataireId + " car il possède des versements.");
            return false;
        }

        allocataireMapper.deleteById(allocataireId);
        logger.info("Allocataire " + allocataireId + " supprimé avec succès.");
        return true;
    }

    public Allocataire findByNoAVS(String noAVS) {
        return allocataireMapper.findByNoAVS(noAVS);
    }
}
