package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import ch.hearc.cafheg.service.AllocationService;
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
  void getParentDroitAllocation_GivenOneParentActive_ShouldReturnActiveParent() {
    ParentDroitAllocationParams params = new ParentDroitAllocationParams();
    params.setParent1ActiviteLucrative(true);
    params.setParent2ActiviteLucrative(false);
    String parentDroitAllocation = allocationService.getParentDroitAllocation(params);
    assertThat(parentDroitAllocation).isEqualTo(AllocationService.PARENT_1);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentActiveOneHasAuthority_ShouldReturnParentWithAuthority() {
    ParentDroitAllocationParams params = new ParentDroitAllocationParams();
    params.setParent1ActiviteLucrative(true);
    params.setParent1AutoriteParentale(true);
    params.setParent2ActiviteLucrative(true);
    String parentDroitAllocation = allocationService.getParentDroitAllocation(params);
    assertThat(parentDroitAllocation).isEqualTo(AllocationService.PARENT_1);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentActiveHaveAuthorityOneLivesWithChild_ShouldReturnParentLivingWithChild() {
    ParentDroitAllocationParams params = new ParentDroitAllocationParams();
    params.setEnfantResidence("NE");
    params.setEnfantVitAvecParent1(true);
    params.setParent1ActiviteLucrative(true);
    params.setParent1AutoriteParentale(true);
    params.setParent1Residence("NE");
    params.setParent2ActiviteLucrative(true);
    params.setParent2AutoriteParentale(true);
    params.setParent2Residence("NE");
    String parentDroitAllocation = allocationService.getParentDroitAllocation(params);
    assertThat(parentDroitAllocation).isEqualTo(AllocationService.PARENT_1);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentActiveHaveAuthorityLiveTogetherButOneWorksInTheChildCanton_ShouldReturnParentWorkingInTheChildCanton() {
    ParentDroitAllocationParams params = new ParentDroitAllocationParams();
    params.setEnfantResidence("NE");
    params.setParent1ActiviteLucrative(true);
    params.setParent1CantonActivite("NE");
    params.setParent1AutoriteParentale(true);
    params.setParent1Residence("NE");
    params.setParent2ActiviteLucrative(true);
    params.setParent2CantonActivite("FR");
    params.setParent2AutoriteParentale(true);
    params.setParent2Residence("NE");
    params.setParentsEnsemble(true);
    String parentDroitAllocation = allocationService.getParentDroitAllocation(params);
    assertThat(parentDroitAllocation).isEqualTo(AllocationService.PARENT_1);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentActiveHaveAuthorityLiveTogetherOneSalariedOneIndependent_ShouldReturnSalariedParent() {
    ParentDroitAllocationParams params = new ParentDroitAllocationParams();
    params.setEnfantResidence("NE");
    params.setParent1ActiviteLucrative(true);
    params.setParent1CantonActivite("NE");
    params.setParent1StatutPro(StatutProfessionnel.SALARIE);
    params.setParent1AutoriteParentale(true);
    params.setParent1Residence("NE");
    params.setParent2ActiviteLucrative(true);
    params.setParent2CantonActivite("NE");
    params.setParent2StatutPro(StatutProfessionnel.INDEPENDANT);
    params.setParent2AutoriteParentale(true);
    params.setParent2Residence("NE");
    params.setParentsEnsemble(true);
    String parentDroitAllocation = allocationService.getParentDroitAllocation(params);
    assertThat(parentDroitAllocation).isEqualTo(AllocationService.PARENT_1);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentActiveHaveAuthorityLiveTogetherBothSalaried_ShouldReturnParentWithHigherSalary() {
    ParentDroitAllocationParams params = new ParentDroitAllocationParams();
    params.setEnfantResidence("NE");
    params.setParent1ActiviteLucrative(true);
    params.setParent1CantonActivite("NE");
    params.setParent1StatutPro(StatutProfessionnel.SALARIE);
    params.setParent1AutoriteParentale(true);
    params.setParent1Residence("NE");
    params.setParent2ActiviteLucrative(true);
    params.setParent2CantonActivite("NE");
    params.setParent2StatutPro(StatutProfessionnel.SALARIE);
    params.setParent2AutoriteParentale(true);
    params.setParent2Residence("NE");
    params.setParentsEnsemble(true);
    params.setParent1Salaire(new Montant(new BigDecimal(1000)));
    params.setParent2Salaire(new Montant(new BigDecimal(2000)));
    String parentDroitAllocation = allocationService.getParentDroitAllocation(params);
    assertThat(parentDroitAllocation).isEqualTo(AllocationService.PARENT_2);
  }

  @Test
  void getParentDroitAllocation_GivenBothParentActiveHaveAuthorityLiveTogetherBothIndependent_ShouldReturnParentWithHigherSalary() {
    ParentDroitAllocationParams params = new ParentDroitAllocationParams();
    params.setEnfantResidence("NE");
    params.setParent1ActiviteLucrative(true);
    params.setParent1CantonActivite("NE");
    params.setParent1StatutPro(StatutProfessionnel.INDEPENDANT);
    params.setParent1AutoriteParentale(true);
    params.setParent1Residence("NE");
    params.setParent2ActiviteLucrative(true);
    params.setParent2CantonActivite("NE");
    params.setParent2StatutPro(StatutProfessionnel.INDEPENDANT);
    params.setParent2AutoriteParentale(true);
    params.setParent2Residence("NE");
    params.setParentsEnsemble(true);
    params.setParent1Salaire(new Montant(new BigDecimal(1000)));
    params.setParent2Salaire(new Montant(new BigDecimal(2000)));
    String parentDroitAllocation = allocationService.getParentDroitAllocation(params);
    assertThat(parentDroitAllocation).isEqualTo(AllocationService.PARENT_2);
  }

}
