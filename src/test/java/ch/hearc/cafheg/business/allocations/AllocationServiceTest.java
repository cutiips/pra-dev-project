package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  // Méthodes pour accéder aux constantes privées via réflexion
  private String getExpectedParent1() {
    try {
      Field field = AllocationService.class.getDeclaredField("PARENT_1");
      field.setAccessible(true);
      return (String) field.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String getExpectedParent2() {
    try {
      Field field = AllocationService.class.getDeclaredField("PARENT_2");
      field.setAccessible(true);
      return (String) field.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Batterie de tests pour getParentDroitAllocation
  @Test
  void getParentDroitAllocation_Parent1OnlyWorking_ShouldReturnParent1() {
    ParentDroitAllocationRequest request = new ParentDroitAllocationRequest();
    request.setParent1ActiviteLucrative(true);
    request.setParent2ActiviteLucrative(false);

    String result = allocationService.getParentDroitAllocation(request);
    assertThat(result).isEqualTo(AllocationService.PARENT_1);
  }


}
