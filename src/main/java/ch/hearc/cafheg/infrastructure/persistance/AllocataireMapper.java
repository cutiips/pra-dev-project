package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllocataireMapper extends Mapper {

  private static final Logger logger = LoggerFactory.getLogger(AllocataireMapper.class);

  private static final String QUERY_FIND_ALL = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES";
  private static final String QUERY_FIND_WHERE_NOM_LIKE = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?";
  private static final String QUERY_FIND_WHERE_NUMERO = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?";
  private static final String QUERY_FIND_WHERE_NO_AVS = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NO_AVS=?";
  private static final String QUERY_DELETE_BY_ID = "DELETE FROM ALLOCATAIRES WHERE NUMERO=?";

  public List<Allocataire> findAll(String likeNom) {
    logger.info("Find all " + likeNom);
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement;
      if (likeNom == null) {
        logger.debug("SQL: " + QUERY_FIND_ALL);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_ALL);
      } else {

        logger.debug("SQL: " + QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement.setString(1, likeNom + "%");
      }
      logger.info("Allocation d'un nouveau tableau");
      List<Allocataire> allocataires = new ArrayList<>();

      logger.info("Exécution de la requête");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {

        logger.info("Allocataire mapping");
        while (resultSet.next()) {
          logger.trace("resultSet#next");
          allocataires
              .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(2),
                  resultSet.getString(1)));
        }
      }
      logger.info("Allocataires trouvés " + allocataires.size());
      return allocataires;
    } catch (SQLException e) {
      logger.error("Erreur SQL", e);
      throw new RuntimeException(e);
    }
  }

  public Allocataire findById(long id) {
    logger.info("findById() " + id);
    Connection connection = activeJDBCConnection();
    try {
      logger.debug("SQL: " + QUERY_FIND_WHERE_NUMERO);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NUMERO);
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      logger.trace("resultSet#next");
      resultSet.next();
      logger.info("Allocataire mapping");
      return new Allocataire(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      logger.error("Erreur SQL", e);
      throw new RuntimeException(e);
    }
  }

  public void updateAllocataire(String noAVS, String nouveauNom, String nouveauPrenom) {
    logger.info("updateAllocataire() - AVS: " + noAVS + ", Nom: " + nouveauNom + ", Prénom: " + nouveauPrenom);
    Connection connection = activeJDBCConnection();

    try {
      Allocataire allocataireExistant = findByNoAVS(noAVS);
      if (allocataireExistant == null) {
        throw new RuntimeException("Aucun allocataire trouvé avec le numéro AVS : " + noAVS);
      }

      if (allocataireExistant.getNom().equals(nouveauNom) && allocataireExistant.getPrenom().equals(nouveauPrenom)) {
        logger.info("Aucune modification à apporter.");
        return;
      }

      String sql = "UPDATE ALLOCATAIRES SET NOM = ?, PRENOM = ? WHERE NO_AVS = ?";
      logger.debug("SQL: " + sql);
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, nouveauNom);
      preparedStatement.setString(2, nouveauPrenom);
      preparedStatement.setString(3, noAVS);

      int rowsAffected = preparedStatement.executeUpdate();
      if (rowsAffected == 0) {
        throw new RuntimeException("Aucune modification effectuée. L'allocataire avec NO_AVS " + noAVS + " n'existe peut-être pas.");
      }

      logger.info("Allocataire mis à jour avec succès !");
    } catch (SQLException e) {
      logger.error("Erreur SQL", e);
      throw new RuntimeException("Erreur lors de la mise à jour de l'allocataire", e);
    }
  }


  public void deleteById(long id) {
    logger.info("deleteById() " + id);
    Connection connection = activeJDBCConnection();
    try {
      logger.debug("SQL: " + QUERY_DELETE_BY_ID);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE_BY_ID);
      preparedStatement.setLong(1, id);
      int rowsAffected = preparedStatement.executeUpdate();
      if (rowsAffected == 0) {
        throw new RuntimeException("Aucun allocataire supprimé. ID non trouvé : " + id);
      }
      logger.info("Allocataire supprimé avec succès !");
    } catch (SQLException e) {
      logger.error("Erreur SQL", e);
      throw new RuntimeException("Erreur lors de la suppression de l'allocataire", e);
    }
  }

  public Allocataire findByNoAVS(String noAVS) {
    logger.info("findByNoAVS() " + noAVS);
    Connection connection = activeJDBCConnection();
    try {
      logger.debug("SQL: " + QUERY_FIND_WHERE_NO_AVS);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NO_AVS);
      preparedStatement.setString(1, noAVS);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        logger.info("Allocataire trouvé, mapping en objet");
        return new Allocataire(new NoAVS(resultSet.getString("NO_AVS")),
                resultSet.getString("NOM"), resultSet.getString("PRENOM"));
      } else {
        logger.info("Aucun allocataire trouvé avec le numéro AVS " + noAVS);
        return null;
      }
    } catch (SQLException e) {
      logger.error("Erreur SQL", e);
      throw new RuntimeException("Erreur lors de la recherche de l'allocataire", e);
    }
  }


}
