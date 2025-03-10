package ch.hearc.cafheg.business.allocations;

import java.math.BigDecimal;

public class ParentDroitAllocationRequest {
    private String enfantResidence = "";
    private Boolean parent1ActiviteLucrative = false;
    private String parent1Residence = "";
    private Boolean parent2ActiviteLucrative = false;
    private String parent2Residence = "";
    private Boolean parentsEnsemble = false;
    private BigDecimal parent1Salaire = BigDecimal.ZERO;
    private BigDecimal parent2Salaire = BigDecimal.ZERO;

    public ParentDroitAllocationRequest() {}

    public String getEnfantResidence() { return enfantResidence; }
    public void setEnfantResidence(String enfantResidence) { this.enfantResidence = enfantResidence; }

    public Boolean getParent1ActiviteLucrative() { return parent1ActiviteLucrative; }
    public void setParent1ActiviteLucrative(Boolean parent1ActiviteLucrative) { this.parent1ActiviteLucrative = parent1ActiviteLucrative; }

    public String getParent1Residence() { return parent1Residence; }
    public void setParent1Residence(String parent1Residence) { this.parent1Residence = parent1Residence; }

    public Boolean getParent2ActiviteLucrative() { return parent2ActiviteLucrative; }
    public void setParent2ActiviteLucrative(Boolean parent2ActiviteLucrative) { this.parent2ActiviteLucrative = parent2ActiviteLucrative; }

    public String getParent2Residence() { return parent2Residence; }
    public void setParent2Residence(String parent2Residence) { this.parent2Residence = parent2Residence; }

    public Boolean getParentsEnsemble() { return parentsEnsemble; }
    public void setParentsEnsemble(Boolean parentsEnsemble) { this.parentsEnsemble = parentsEnsemble; }

    public BigDecimal getParent1Salaire() { return parent1Salaire; }
    public void setParent1Salaire(BigDecimal parent1Salaire) { this.parent1Salaire = parent1Salaire; }

    public BigDecimal getParent2Salaire() { return parent2Salaire; }
    public void setParent2Salaire(BigDecimal parent2Salaire) { this.parent2Salaire = parent2Salaire; }
}
