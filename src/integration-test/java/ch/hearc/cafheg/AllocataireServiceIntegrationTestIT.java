package ch.hearc.cafheg;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import ch.hearc.cafheg.service.AllocataireService;
import ch.hearc.cafheg.business.versements.VersementParentEnfant;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AllocataireServiceIntegrationTestIT {


    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private IDatabaseTester databaseTester;
    private AllocataireService allocataireService;

    @BeforeAll
    void createSchema() throws Exception {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            connection.createStatement().execute(
                    "CREATE TABLE ALLOCATAIRES (" +
                            "  NUMERO INT PRIMARY KEY," +
                            "  NO_AVS VARCHAR(50)," +
                            "  NOM VARCHAR(50)," +
                            "  PRENOM VARCHAR(50)" +
                            ");"
            );
        }

        databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
    }

    @BeforeEach
    void importDataSet() throws Exception {
        InputStream xml = getClass().getClassLoader().getResourceAsStream("dataset-allocataires.xml");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(xml);

        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), dataSet);

        AllocataireMapper allocataireMapper = new AllocataireMapper() {
            @Override
            protected Connection activeJDBCConnection() {
                try {
                    return databaseTester.getConnection().getConnection();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        VersementMapper versementMapper = new VersementMapper() {
            @Override
            protected Connection activeJDBCConnection() {
                try {
                    return databaseTester.getConnection().getConnection();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public List<VersementParentEnfant> findVersementParentEnfant() {
                return new ArrayList<>();
            }
        };
        allocataireService = new AllocataireService(allocataireMapper, versementMapper);
    }

    @Test
    void testSuppressionAllocataire() throws Exception {
        boolean deleted = allocataireService.deleteAllocataire(2L);
        assertThat(deleted).isTrue();

        try (Connection c = databaseTester.getConnection().getConnection()) {
            ResultSet rs = c.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM ALLOCATAIRES WHERE NUMERO=2");
            rs.next();
            assertThat(rs.getInt(1)).isEqualTo(0);
        }
    }

    @Test
    void testModificationAllocataire() throws Exception {
        boolean updated = allocataireService.updateAllocataire("756.1111.1111.11", "Dupuis", "Julien");
        assertThat(updated).isTrue();

        try (Connection c = databaseTester.getConnection().getConnection()) {
            ResultSet rs = c.createStatement()
                    .executeQuery("SELECT NOM, PRENOM FROM ALLOCATAIRES WHERE NO_AVS='756.1111.1111.11'");
            rs.next();
            assertThat(rs.getString("NOM")).isEqualTo("Dupuis");
            assertThat(rs.getString("PRENOM")).isEqualTo("Julien");
        }
    }
}