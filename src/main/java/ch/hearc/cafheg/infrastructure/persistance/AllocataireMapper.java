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
}
