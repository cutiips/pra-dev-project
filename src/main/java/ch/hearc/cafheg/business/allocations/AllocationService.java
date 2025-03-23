package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class AllocationService {

  private static final Logger logger = LoggerFactory.getLogger(AllocationService.class);

  public static final String PARENT_1 = "Parent1";
  public static final String PARENT_2 = "Parent2";

  private final AllocataireMapper allocataireMapper;
  private final AllocationMapper allocationMapper;

  public AllocationService(
      AllocataireMapper allocataireMapper,
      AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

  public List<Allocataire> findAllAllocataires(String likeNom) {
    logger.info("Rechercher tous les allocataires");
    return allocataireMapper.findAll(likeNom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

//  public String getParentDroitAllocation(ParentDroitAllocationParams params) {
//    logger.info("Déterminer quel parent a le droit aux allocations");
//    String eR = params.getEnfantResidence();
//    boolean p1AL = params.isParent1ActiviteLucrative();
//    String p1Residence = params.getParent1Residence();
//    boolean p2AL = params.isParent2ActiviteLucrative();
//    String p2Residence = params.getParent2Residence();
//    boolean pEnsemble = params.isParentsEnsemble();
//    Montant salaireP1 = params.getParent1Salaire();
//    Montant salaireP2 = params.getParent2Salaire();
//
//    if(p1AL && !p2AL) {
//      return PARENT_1;
//    }
//
//    if(p2AL && !p1AL) {
//      return PARENT_2;
//    }
//
//    return salaireP1.getValue().doubleValue() > salaireP2.getValue().doubleValue() ? PARENT_1 : PARENT_2;
//  }

  public String getParentDroitAllocation(ParentDroitAllocationParams params) {
    logger.info("Déterminer quel parent a le droit aux allocations");
    String enfantResidence = params.getEnfantResidence();
    boolean enfantVitAvecP1 = params.isEnfantVitAvecParent1();
    boolean p1AL = params.isParent1ActiviteLucrative();
    String p1CantonAL = params.getParent1CantonActivite();
    StatutProfessionnel p1StatutPro = params.getParent1StatutPro();
    boolean p1Autorite = params.isParent1AutoriteParentale();
    boolean p2AL = params.isParent2ActiviteLucrative();
    String p2CantonAL = params.getParent2CantonActivite();
    StatutProfessionnel p2StatutPro = params.getParent2StatutPro();
    boolean p2Autorite = params.isParent2AutoriteParentale();
    boolean parentsEnsemble = params.isParentsEnsemble();
    BigDecimal p1Salaire = (params.getParent1Salaire() == null)
            ? BigDecimal.ZERO
            : params.getParent1Salaire().getValue();
    BigDecimal p2Salaire = (params.getParent2Salaire() == null)
            ? BigDecimal.ZERO
            : params.getParent2Salaire().getValue();

    if (p1AL && !p2AL) {
      return PARENT_1;
    }
    if (p2AL && !p1AL) {
      return PARENT_2;
    }

    if (p1AL && p2AL) {

      if (p1Autorite && !p2Autorite) {
        return PARENT_1;
      }
      if (p2Autorite && !p1Autorite) {
        return PARENT_2;
      }
      if (!p1Autorite && !p2Autorite) {
        return "Autorité parentale non définie";
      }

      if (!parentsEnsemble) {
        return enfantVitAvecP1 ? PARENT_1 : PARENT_2;
      }

      boolean p1TravailleCantonEnfant = (p1CantonAL != null && p1CantonAL.equals(enfantResidence));
      boolean p2TravailleCantonEnfant = (p2CantonAL != null && p2CantonAL.equals(enfantResidence));
      if (p1TravailleCantonEnfant && !p2TravailleCantonEnfant) {
        return PARENT_1;
      }
      if (p2TravailleCantonEnfant && !p1TravailleCantonEnfant) {
        return PARENT_2;
      }

      if (p1StatutPro == StatutProfessionnel.SALARIE && p2StatutPro == StatutProfessionnel.INDEPENDANT) {
        return PARENT_1;
      }
      if (p2StatutPro == StatutProfessionnel.SALARIE && p1StatutPro == StatutProfessionnel.INDEPENDANT) {
        return PARENT_2;
      }

      return (p1Salaire.compareTo(p2Salaire) > 0) ? PARENT_1 : PARENT_2;
    }

    return "Aucun droit";
  }

}
