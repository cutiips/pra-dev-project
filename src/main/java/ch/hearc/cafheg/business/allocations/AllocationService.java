package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AllocationService {

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
    System.out.println("Rechercher tous les allocataires");
    return allocataireMapper.findAll(likeNom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  public String getParentDroitAllocation(ParentDroitAllocationRequest request) {
    Boolean p1AL = request.getParent1ActiviteLucrative();
    Boolean p2AL = request.getParent2ActiviteLucrative();
    BigDecimal salaireP1 = request.getParent1Salaire();
    BigDecimal salaireP2 = request.getParent2Salaire();

    if (p1AL && !p2AL) {
      return PARENT_1;
    }
    if (p2AL && !p1AL) {
      return PARENT_2;
    }
    return salaireP1.doubleValue() > salaireP2.doubleValue() ? PARENT_1 : PARENT_2;
  }

}
