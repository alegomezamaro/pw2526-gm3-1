package es.uco.pw.demo.model;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Repository
public class PatronRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
   private String sqlQueriesFileName = "db/sql.properties";
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
            // 'sqlQueriesFileName' debe ser "db/sql.properties"
            InputStream is = getClass().getClassLoader().getResourceAsStream(sqlQueriesFileName);

            if (is == null) {
                System.err.println("ERROR: El fichero de properties NO se encontró en el classpath: " + sqlQueriesFileName);
                return;
            }

            // Usamos UTF-8 para leer tildes/eñes
            sqlQueries.load(new InputStreamReader(is, StandardCharsets.UTF_8));
            is.close();
            
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

    public Patron findPatronByDNI(String dni){
        String query = sqlQueries.getProperty("select-findPatronByDNI");
        // RowMapper manual para MySQL 5.1.49
        List<Patron> patrones = jdbcTemplate.query(query, new RowMapper<Patron>() {
            public Patron mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                return new Patron(
                    rs.getInt("id"),
                    rs.getString("dni"),  
                    rs.getString("nombre"),  
                    rs.getString("apellidos"), 
                    LocalDate.parse(rs.getString("fechaNacimiento")),
                    LocalDate.parse(rs.getString("fechaTituloPatron"))
                );
            }
        }, dni);
        return patrones.isEmpty() ? null : patrones.get(0);

    }


    public boolean updatePatron(Patron Patron) {
        String query = sqlQueries.getProperty("update-updatePatron2");
        try {
            int rows = jdbcTemplate.update(query,
                Patron.getNombre(),
                Patron.getApellidos(),
                Patron.getFechaNacimiento(),
                Patron.getFechaTituloPatron(),
                Patron.getDni()
            );
            return rows > 0 ;
        } catch (DataAccessException e) {
            System.err.println("Unable to update Patron in the db");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePatronByDni(String dni) {
        String query = sqlQueries.getProperty("delete-deletePatronByDNI");
        try {
            int rows = jdbcTemplate.update(query,dni);
            return rows > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to delete socio in the db");
            return false;
        }
    }
}
