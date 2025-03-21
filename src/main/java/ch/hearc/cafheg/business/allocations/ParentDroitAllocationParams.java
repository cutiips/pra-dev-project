package ch.hearc.cafheg.business.allocations;

import java.math.BigDecimal;

public class ParentDroitAllocationParams {
    private String enfantResidence;
    private boolean parent1ActiviteLucrative;
    private String parent1Residence;
    private boolean parent2ActiviteLucrative;
    private String parent2Residence;
    private boolean parentsEnsemble;
    private BigDecimal parent1Salaire;
    private BigDecimal parent2Salaire;

    public ParentDroitAllocationParams(String enfantResidence, boolean parent1ActiviteLucrative, String parent1Residence, boolean parent2ActiviteLucrative, String parent2Residence, boolean parentsEnsemble, BigDecimal parent1Salaire, BigDecimal parent2Salaire) {
        this.enfantResidence = enfantResidence;
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
        this.parent1Residence = parent1Residence;
        this.parent2ActiviteLucrative = parent2ActiviteLucrative;
        this.parent2Residence = parent2Residence;
        this.parentsEnsemble = parentsEnsemble;
        this.parent1Salaire = parent1Salaire;
        this.parent2Salaire = parent2Salaire;
    }

    public ParentDroitAllocationParams() {
    }

    public String getEnfantResidence() {
        return enfantResidence;
    }

    public void setEnfantResidence(String enfantResidence) {
        this.enfantResidence = enfantResidence;
    }

    public boolean isParent1ActiviteLucrative() {
        return parent1ActiviteLucrative;
    }

    public void setParent1ActiviteLucrative(boolean parent1ActiviteLucrative) {
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
    }

    public String getParent1Residence() {
        return parent1Residence;
    }

    public void setParent1Residence(String parent1Residence) {
        this.parent1Residence = parent1Residence;
    }

    public boolean isParent2ActiviteLucrative() {
        return parent2ActiviteLucrative;
    }

    public void setParent2ActiviteLucrative(boolean parent2ActiviteLucrative) {
        this.parent2ActiviteLucrative = parent2ActiviteLucrative;
    }

    public String getParent2Residence() {
        return parent2Residence;
    }

    public void setParent2Residence(String parent2Residence) {
        this.parent2Residence = parent2Residence;
    }

    public boolean isParentsEnsemble() {
        return parentsEnsemble;
    }

    public void setParentsEnsemble(boolean parentsEnsemble) {
        this.parentsEnsemble = parentsEnsemble;
    }

    public BigDecimal getParent1Salaire() {
        return parent1Salaire;
    }

    public void setParent1Salaire(BigDecimal parent1Salaire) {
        this.parent1Salaire = parent1Salaire;
    }

    public BigDecimal getParent2Salaire() {
        return parent2Salaire;
    }

    public void setParent2Salaire(BigDecimal parent2Salaire) {
        this.parent2Salaire = parent2Salaire;
    }
}
