package ch.hearc.cafheg.business.allocations;

public class Allocataire {

  private final NoAVS noAVS;
  private String nom;
  private String prenom;

  public Allocataire(NoAVS noAVS, String nom, String prenom) {
    this.noAVS = noAVS;
    this.nom = nom;
    this.prenom = prenom;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom ;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public NoAVS getNoAVS() {
    return noAVS;
  }
}
