package es.uco.pw.demo.model;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Repository
public class PatronRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
   private String sqlQueriesFileName = "src/main/resources/db/sql.properties";
    public PatronRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    private void createProperties() {
        sqlQueries = new Properties();
        try {
            BufferedReader reader;
            File f = new File(sqlQueriesFileName);
            reader = new BufferedReader(new FileReader(f));
            sqlQueries.load(reader);
        } catch (IOException e) {
            System.err.println("Error creating properties object for SQL Queries");
            e.printStackTrace();
        }
    }

    public List<Patron> findAllPatrones() {
        try {
            if (sqlQueries == null) createProperties();

            String query = sqlQueries.getProperty("select-findAllPatrones");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Patron>() {
                    @Override
                    public Patron mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Patron(
                            rs.getInt("id"),
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getDate("fechanacimiento") != null
                                ? rs.getDate("fechanacimiento").toLocalDate()
                                : null,
                            rs.getDate("fechatitulopatron") != null
                                ? rs.getDate("fechatitulopatron").toLocalDate()
                                : null
                        );
                    }
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find patrones");
            e.printStackTrace();
            return null;
        }
    }

    public boolean addPatron(Patron p) {
        try {

            if (sqlQueries == null) createProperties();

            String query = sqlQueries.getProperty("insert-addPatron");
            if (query != null) {
                int result = jdbcTemplate.update(
                    query,
                    p.getId(),
                    p.getDni(),
                    p.getNombre(),
                    p.getApellidos(),
                    p.getFechaNacimiento() != null ? Date.valueOf(p.getFechaNacimiento()) : null,
                    p.getFechaTituloPatron() != null ? Date.valueOf(p.getFechaTituloPatron()) : null
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to insert patron into the db");
            ex.printStackTrace();
            return false;
        }
    }
}
