package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllocataireMapper extends Mapper {

  private static final String QUERY_FIND_ALL = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES";
  private static final String QUERY_FIND_WHERE_NOM_LIKE = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?";
  private static final String QUERY_FIND_WHERE_NUMERO = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?";

  private static final String QUERY_DELETE_BY_ID = "DELETE FROM ALLOCATAIRES WHERE NUMERO=?";

  public List<Allocataire> findAll(String likeNom) {
    System.out.println("findAll() " + likeNom);
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement;
      if (likeNom == null) {
        System.out.println("SQL: " + QUERY_FIND_ALL);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_ALL);
      } else {

        System.out.println("SQL: " + QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement.setString(1, likeNom + "%");
      }
      System.out.println("Allocation d'un nouveau tableau");
      List<Allocataire> allocataires = new ArrayList<>();

      System.out.println("Exécution de la requête");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {

        System.out.println("Allocataire mapping");
        while (resultSet.next()) {
          System.out.println("ResultSet#next");
          allocataires
              .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(2),
                  resultSet.getString(1)));
        }
      }
      System.out.println("Allocataires trouvés " + allocataires.size());
      return allocataires;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Allocataire findById(long id) {
    System.out.println("findById() " + id);
    Connection connection = activeJDBCConnection();
    try {
      System.out.println("SQL:" + QUERY_FIND_WHERE_NUMERO);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NUMERO);
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      System.out.println("ResultSet#next");
      resultSet.next();
      System.out.println("Allocataire mapping");
      return new Allocataire(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateAllocataire(String noAVS, String nouveauNom, String nouveauPrenom) {
    System.out.println("updateAllocataire() - AVS: " + noAVS + ", Nom: " + nouveauNom + ", Prénom: " + nouveauPrenom);
    Connection connection = activeJDBCConnection();

    try {
      // Vérifier si l'allocataire existe et récupérer ses valeurs actuelles
      Allocataire allocataireExistant = findByNoAVS(noAVS);
      if (allocataireExistant == null) {
        throw new RuntimeException("Aucun allocataire trouvé avec le numéro AVS : " + noAVS);
      }

      // Vérifier si le nom ou le prénom a changé
      if (allocataireExistant.getNom().equals(nouveauNom) && allocataireExistant.getPrenom().equals(nouveauPrenom)) {
        System.out.println("Aucune modification à apporter.");
        return;
      }

      // Requête SQL pour la mise à jour
      String sql = "UPDATE ALLOCATAIRES SET NOM = ?, PRENOM = ? WHERE NO_AVS = ?";
      System.out.println("SQL: " + sql);
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, nouveauNom);
      preparedStatement.setString(2, nouveauPrenom);
      preparedStatement.setString(3, noAVS);

      // Exécuter la mise à jour
      int rowsAffected = preparedStatement.executeUpdate();
      if (rowsAffected == 0) {
        throw new RuntimeException("Aucune modification effectuée. L'allocataire avec NO_AVS " + noAVS + " n'existe peut-être pas.");
      }

      System.out.println("Allocataire mis à jour avec succès !");
    } catch (SQLException e) {
      throw new RuntimeException("Erreur lors de la mise à jour de l'allocataire", e);
    }
  }


  public void deleteById(long id) {
    System.out.println("deleteById() " + id);
    Connection connection = activeJDBCConnection();
    try {
      System.out.println("SQL: " + QUERY_DELETE_BY_ID);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE_BY_ID);
      preparedStatement.setLong(1, id);
      int rowsAffected = preparedStatement.executeUpdate();
      if (rowsAffected == 0) {
        throw new RuntimeException("Aucun allocataire supprimé. ID non trouvé : " + id);
      }
      System.out.println("Allocataire supprimé avec succès !");
    } catch (SQLException e) {
      throw new RuntimeException("Erreur lors de la suppression de l'allocataire", e);
    }
  }

  public Allocataire findByNoAVS(String noAVS) {
    System.out.println("findByNoAVS() " + noAVS);
    Connection connection = activeJDBCConnection();
    try {
      System.out.println("SQL:" + QUERY_FIND_WHERE_NUMERO);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NUMERO);
      preparedStatement.setString(1, noAVS);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        System.out.println("Allocataire trouvé, mapping en objet");
        return new Allocataire(new NoAVS(resultSet.getString("NO_AVS")),
                resultSet.getString("NOM"), resultSet.getString("PRENOM"));
      } else {
        System.out.println("Aucun allocataire trouvé avec le numéro AVS " + noAVS);
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erreur lors de la recherche de l'allocataire", e);
    }
  }

}
