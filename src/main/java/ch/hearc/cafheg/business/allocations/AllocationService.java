package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  public String getParentDroitAllocation(ParentDroitAllocationParams params) {
    logger.info("Déterminer quel parent a le droit aux allocations");
    String eR = params.getEnfantResidence();
    boolean p1AL = params.isParent1ActiviteLucrative();
    String p1Residence = params.getParent1Residence();
    boolean p2AL = params.isParent2ActiviteLucrative();
    String p2Residence = params.getParent2Residence();
    boolean pEnsemble = params.isParentsEnsemble();
    Number salaireP1 = params.getParent1Salaire();
    Number salaireP2 = params.getParent2Salaire();

    if(p1AL && !p2AL) {
      return PARENT_1;
    }

    if(p2AL && !p1AL) {
      return PARENT_2;
    }

    return salaireP1.doubleValue() > salaireP2.doubleValue() ? PARENT_1 : PARENT_2;
  }

}
