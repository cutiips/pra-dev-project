package ch.hearc.cafheg.business.allocataires;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.hearc.cafheg.business.allocations.AllocataireService;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;

import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class AllocataireServiceTest {

    private AllocataireService allocataireService;
    private AllocataireMapper allocataireMapper;
    private VersementMapper versementMapper;

    @BeforeEach
    void setUp() {
        allocataireMapper = Mockito.mock(AllocataireMapper.class);
        versementMapper = Mockito.mock(VersementMapper.class);
        allocataireService = new AllocataireService(allocataireMapper, versementMapper);
    }

    @Test
    void findAllAllocataires_ShouldReturnEmptyList_WhenNoAllocataires() {
        when(allocataireMapper.findAll("Dupont")).thenReturn(Collections.emptyList());

        List<Allocataire> result = allocataireService.findAllAllocataires("Dupont");

        assertThat(result).isEmpty();
        verify(allocataireMapper, times(1)).findAll("Dupont");
    }

    @Test
    void findAllAllocataires_ShouldReturnList_WhenAllocatairesExist() {
        List<Allocataire> mockAllocataires = Arrays.asList(
                new Allocataire(new NoAVS("1000-2000"), "Durand", "Pierre"),
                new Allocataire(new NoAVS("1000-2001"), "Durand", "Sophie")
        );

        when(allocataireMapper.findAll("Durand")).thenReturn(mockAllocataires);

        List<Allocataire> result = allocataireService.findAllAllocataires("Durand");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNom()).isEqualTo("Durand");
        assertThat(result.get(0).getPrenom()).isEqualTo("Pierre");
        assertThat(result.get(1).getNom()).isEqualTo("Durand");
        assertThat(result.get(1).getPrenom()).isEqualTo("Sophie");
    }

    @Test
    void deleteAllocataire_ShouldCallMapper_WhenAllocataireExists() {
        long allocataireId = 101L;

        allocataireService.supprimerAllocataire(allocataireId);

        verify(allocataireMapper, times(1)).deleteById(allocataireId);
    }

    @Test
    void deleteAllocataire_ShouldThrowException_WhenAllocataireNotFound() {
        long allocataireId = 999L;
        doThrow(new IllegalArgumentException("Allocataire introuvable."))
                .when(allocataireMapper).deleteById(allocataireId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            allocataireService.supprimerAllocataire(allocataireId);
        });

        assertThat(exception.getMessage()).isEqualTo("Allocataire introuvable.");
    }
}
