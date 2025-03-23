package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.common.Montant;

public class ParentDroitAllocationParams {
    private String enfantResidence;
    private boolean enfantVitAvecParent1;
    private boolean parent1ActiviteLucrative;
    private String parent1CantonActivite;
    private StatutProfessionnel parent1StatutPro;
    private boolean parent1AutoriteParentale;
    private String parent1Residence;
    private boolean parent2ActiviteLucrative;
    private String parent2CantonActivite;
    private StatutProfessionnel parent2StatutPro;
    private boolean parent2AutoriteParentale;
    private String parent2Residence;
    private boolean parentsEnsemble;
    private Montant parent1Salaire;
    private Montant parent2Salaire;

    public ParentDroitAllocationParams(String enfantResidence, boolean enfantVitAvecParent1, boolean parent1ActiviteLucrative, String parent1CantonActivite, StatutProfessionnel parent1StatutPro, boolean parent1AutoriteParentale, String parent1Residence, boolean parent2ActiviteLucrative, String parent2CantonActivite, StatutProfessionnel parent2StatutPro, boolean parent2AutoriteParentale, String parent2Residence, boolean parentsEnsemble, Montant parent1Salaire, Montant parent2Salaire) {
        this.enfantResidence = enfantResidence;
        this.enfantVitAvecParent1 = enfantVitAvecParent1;
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
        this.parent1CantonActivite = parent1CantonActivite;
        this.parent1StatutPro = parent1StatutPro;
        this.parent1AutoriteParentale = parent1AutoriteParentale;
        this.parent1Residence = parent1Residence;
        this.parent2ActiviteLucrative = parent2ActiviteLucrative;
        this.parent2CantonActivite = parent2CantonActivite;
        this.parent2StatutPro = parent2StatutPro;
        this.parent2AutoriteParentale = parent2AutoriteParentale;
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

    public boolean isEnfantVitAvecParent1() {
        return enfantVitAvecParent1;
    }

    public void setEnfantVitAvecParent1(boolean enfantVitAvecParent1) {
        this.enfantVitAvecParent1 = enfantVitAvecParent1;
    }

    public boolean isParent1ActiviteLucrative() {
        return parent1ActiviteLucrative;
    }

    public void setParent1ActiviteLucrative(boolean parent1ActiviteLucrative) {
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
    }

    public String getParent1CantonActivite() {
        return parent1CantonActivite;
    }

    public void setParent1CantonActivite(String parent1CantonActivite) {
        this.parent1CantonActivite = parent1CantonActivite;
    }

    public StatutProfessionnel getParent1StatutPro() {
        return parent1StatutPro;
    }

    public void setParent1StatutPro(StatutProfessionnel parent1StatutPro) {
        this.parent1StatutPro = parent1StatutPro;
    }

    public boolean isParent1AutoriteParentale() {
        return parent1AutoriteParentale;
    }

    public void setParent1AutoriteParentale(boolean parent1AutoriteParentale) {
        this.parent1AutoriteParentale = parent1AutoriteParentale;
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

    public String getParent2CantonActivite() {
        return parent2CantonActivite;
    }

    public void setParent2CantonActivite(String parent2CantonActivite) {
        this.parent2CantonActivite = parent2CantonActivite;
    }

    public StatutProfessionnel getParent2StatutPro() {
        return parent2StatutPro;
    }

    public void setParent2StatutPro(StatutProfessionnel parent2StatutPro) {
        this.parent2StatutPro = parent2StatutPro;
    }

    public boolean isParent2AutoriteParentale() {
        return parent2AutoriteParentale;
    }

    public void setParent2AutoriteParentale(boolean parent2AutoriteParentale) {
        this.parent2AutoriteParentale = parent2AutoriteParentale;
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

    public Montant getParent1Salaire() {
        return parent1Salaire;
    }

    public void setParent1Salaire(Montant parent1Salaire) {
        this.parent1Salaire = parent1Salaire;
    }

    public Montant getParent2Salaire() {
        return parent2Salaire;
    }

    public void setParent2Salaire(Montant parent2Salaire) {
        this.parent2Salaire = parent2Salaire;
    }
}
