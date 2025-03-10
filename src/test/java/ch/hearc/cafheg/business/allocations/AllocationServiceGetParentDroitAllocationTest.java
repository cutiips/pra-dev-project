package ch.hearc.cafheg.business.allocations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllocationServiceGetParentDroitAllocationTest {

    private AllocationService allocationService;

    @BeforeEach
    void setUp() {
        // Les mappers ne sont pas utilisés dans cette méthode donc null suffit
        allocationService = new AllocationService(null, null);
    }

    @Test
    void testOnlyParent1ActiviteLucrative() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent1ActiviteLucrative", true);
        params.put("parent2ActiviteLucrative", false);
        params.put("parent1Salaire", 2000);
        params.put("parent2Salaire", 3000);
        String result = allocationService.getParentDroitAllocation(params);
        assertEquals(AllocationService.PARENT_1, result);
    }

    @Test
    void testOnlyParent2ActiviteLucrative() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent1ActiviteLucrative", false);
        params.put("parent2ActiviteLucrative", true);
        params.put("parent1Salaire", 3000);
        params.put("parent2Salaire", 2000);
        String result = allocationService.getParentDroitAllocation(params);
        assertEquals(AllocationService.PARENT_2, result);
    }

    @Test
    void testBothParentsActiviteLucrativeHigherSalaryParent1() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent1ActiviteLucrative", true);
        params.put("parent2ActiviteLucrative", true);
        params.put("parent1Salaire", 4000);
        params.put("parent2Salaire", 3000);
        String result = allocationService.getParentDroitAllocation(params);
        assertEquals(AllocationService.PARENT_1, result);
    }

    @Test
    void testBothParentsActiviteLucrativeHigherSalaryParent2() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent1ActiviteLucrative", true);
        params.put("parent2ActiviteLucrative", true);
        params.put("parent1Salaire", 2500);
        params.put("parent2Salaire", 3500);
        String result = allocationService.getParentDroitAllocation(params);
        assertEquals(AllocationService.PARENT_2, result);
    }

    @Test
    void testNeitherParentActiviteLucrativeHigherSalaryParent1() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent1ActiviteLucrative", false);
        params.put("parent2ActiviteLucrative", false);
        params.put("parent1Salaire", 5000);
        params.put("parent2Salaire", 4000);
        String result = allocationService.getParentDroitAllocation(params);
        assertEquals(AllocationService.PARENT_1, result);
    }

    @Test
    void testNeitherParentActiviteLucrativeHigherSalaryParent2() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent1ActiviteLucrative", false);
        params.put("parent2ActiviteLucrative", false);
        params.put("parent1Salaire", 3000);
        params.put("parent2Salaire", 4000);
        String result = allocationService.getParentDroitAllocation(params);
        assertEquals(AllocationService.PARENT_2, result);
    }

    @Test
    void testEqualSalaryBothActiviteLucrative() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent1ActiviteLucrative", true);
        params.put("parent2ActiviteLucrative", true);
        params.put("parent1Salaire", 3000);
        params.put("parent2Salaire", 3000);
        String result = allocationService.getParentDroitAllocation(params);
        // Si les salaires sont égaux, la condition > est fausse : retourne PARENT_2
        assertEquals(AllocationService.PARENT_2, result);
    }

    @Test
    void testEqualSalaryNeitherActiviteLucrative() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent1ActiviteLucrative", false);
        params.put("parent2ActiviteLucrative", false);
        params.put("parent1Salaire", 3000);
        params.put("parent2Salaire", 3000);
        String result = allocationService.getParentDroitAllocation(params);
        assertEquals(AllocationService.PARENT_2, result);
    }

    @Test
    void testEmptyMapDefaults() {
        Map<String, Object> params = new HashMap<>();
        String result = allocationService.getParentDroitAllocation(params);
        // Valeurs par défaut : aucun parent actif, salaires à zéro → retourne PARENT_2
        assertEquals(AllocationService.PARENT_2, result);
    }
}
