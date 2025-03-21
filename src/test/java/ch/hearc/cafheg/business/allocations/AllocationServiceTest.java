package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AllocationServiceTest {

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);

    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }

  @Test
  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertThat(all).isEmpty();
  }

  @Test
  void findAllAllocataires_Given2Geiser_ShouldBe2() {
    Mockito.when(allocataireMapper.findAll("Geiser"))
            .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
                    new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertAll(
            () -> assertThat(all.size()).isEqualTo(2),
            () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
            () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
            () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
            () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
            () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
            () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie")
    );
  }

  @Test
  void findAllocationsActuelles() {
    Mockito.when(allocationMapper.findAll())
            .thenReturn(Arrays.asList(
                    new Allocation(new Montant(new BigDecimal(1000)), Canton.NE, LocalDate.now(), null),
                    new Allocation(new Montant(new BigDecimal(2000)), Canton.FR, LocalDate.now(), null)));
    List<Allocation> all = allocationService.findAllocationsActuelles();
    assertAll(
            () -> assertThat(all.size()).isEqualTo(2),
            () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
            () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
            () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
            () -> assertThat(all.get(0).getFin()).isNull(),
            () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
            () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
            () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
            () -> assertThat(all.get(1).getFin()).isNull()
    );
  }

    @Test
    void getParentDroitAllocation_GivenParent1ActiveParent2Inactive_ShouldReturnParent1() {
      ParentDroitAllocationParams params = new ParentDroitAllocationParams();
      params.setParent1ActiviteLucrative(true);
      params.setParent2ActiviteLucrative(false);
      params.setEnfantResidence("");
      params.setParent1Residence("");
      params.setParent2Residence("");
      params.setParentsEnsemble(true);
      params.setParent1Salaire(BigDecimal.valueOf(4000));
      params.setParent2Salaire(BigDecimal.valueOf(2000));

      String result = allocationService.getParentDroitAllocation(params);

      assertThat(result).isEqualTo(AllocationService.PARENT_1);
    }

    @Test
    void getParentDroitAllocation_GivenParent2ActiveParent1Inactive_ShouldReturnParent2() {
      ParentDroitAllocationParams params = new ParentDroitAllocationParams();
      params.setParent1ActiviteLucrative(false);
      params.setParent2ActiviteLucrative(true);
      params.setEnfantResidence("");
      params.setParent1Residence("");
      params.setParent2Residence("");
      params.setParentsEnsemble(true);
      params.setParent1Salaire(BigDecimal.valueOf(3000));
      params.setParent2Salaire(BigDecimal.valueOf(5000));

      String result = allocationService.getParentDroitAllocation(params);

      assertThat(result).isEqualTo(AllocationService.PARENT_2);
    }

    @Test
    void getParentDroitAllocation_GivenBothActiveAndParent1HasHigherSalary_ShouldReturnParent1() {
      ParentDroitAllocationParams params = new ParentDroitAllocationParams();
      params.setParent1ActiviteLucrative(true);
      params.setParent2ActiviteLucrative(true);
      params.setEnfantResidence("");
      params.setParent1Residence("");
      params.setParent2Residence("");
      params.setParentsEnsemble(false);
      params.setParent1Salaire(BigDecimal.valueOf(6000));
      params.setParent2Salaire(BigDecimal.valueOf(4000));

      String result = allocationService.getParentDroitAllocation(params);

      assertThat(result).isEqualTo(AllocationService.PARENT_1);
    }

    @Test
    void getParentDroitAllocation_GivenBothActiveAndParent2HasHigherSalary_ShouldReturnParent2() {
      ParentDroitAllocationParams params = new ParentDroitAllocationParams();
      params.setParent1ActiviteLucrative(true);
      params.setParent2ActiviteLucrative(true);
      params.setEnfantResidence("");
      params.setParent1Residence("");
      params.setParent2Residence("");
      params.setParentsEnsemble(false);
      params.setParent1Salaire(BigDecimal.valueOf(2000));
      params.setParent2Salaire(BigDecimal.valueOf(5000));

      String result = allocationService.getParentDroitAllocation(params);

      assertThat(result).isEqualTo(AllocationService.PARENT_2);
    }

    @Test
    void getParentDroitAllocation_GivenEqualSalaries_ShouldReturnParent2() {
      ParentDroitAllocationParams params = new ParentDroitAllocationParams();
      params.setParent1ActiviteLucrative(true);
      params.setParent2ActiviteLucrative(true);
      params.setEnfantResidence("");
      params.setParent1Residence("");
      params.setParent2Residence("");
      params.setParentsEnsemble(false);
      params.setParent1Salaire(BigDecimal.valueOf(4000));
      params.setParent2Salaire(BigDecimal.valueOf(4000));

      String result = allocationService.getParentDroitAllocation(params);

      assertThat(result).isEqualTo(AllocationService.PARENT_2);
    }

    @Test
    void getParentDroitAllocation_GivenBothInactive_ShouldReturnBySalaryComparison() {
      ParentDroitAllocationParams params = new ParentDroitAllocationParams();
      params.setParent1ActiviteLucrative(false);
      params.setParent2ActiviteLucrative(false);
      params.setEnfantResidence("");
      params.setParent1Residence("");
      params.setParent2Residence("");
      params.setParentsEnsemble(false);
      params.setParent1Salaire(BigDecimal.valueOf(1000));
      params.setParent2Salaire(BigDecimal.valueOf(500));

      String result = allocationService.getParentDroitAllocation(params);

      assertThat(result).isEqualTo(AllocationService.PARENT_1);
    }

}
