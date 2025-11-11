package es.uco.pw.demo.model;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Repository
public class EmbarcacionRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName = "src/main/resources/db/sql.properties";

    public EmbarcacionRepository(JdbcTemplate jdbcTemplate) {
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
            System.err.println("Error creating properties object for SQL Queries (" + sqlQueriesFileName + ")");
            e.printStackTrace();
        }
    }

    public List<Embarcacion> findAllEmbarcaciones() {
        try {
            // ✅ Carga perezosa para evitar NullPointerException
            if (sqlQueries == null) createProperties();

            String query = (sqlQueries != null) ? sqlQueries.getProperty("select-findAllEmbarcaciones") : null;
            if (query != null) {
                List<Embarcacion> embarcaciones = jdbcTemplate.query(query, new RowMapper<Embarcacion>() {
                    @Override
                    public Embarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Embarcacion(
                            rs.getString("matricula"),
                            rs.getString("nombre"),
                            EmbarcacionType.valueOf(rs.getString("tipo")),
                            rs.getInt("plazas"),
                            rs.getString("dimensiones"),
                            /* patronAsignado */ null
                        );
                    }
                });
                return embarcaciones;
            } else return null;
        } catch (DataAccessException e) {
            System.err.println("Unable to find embarcaciones");
            e.printStackTrace();
            return null;
        }
    }

    public boolean addEmbarcacion(Embarcacion e) {
        try {
            // ✅ Carga perezosa para evitar NullPointerException
            if (sqlQueries == null) createProperties();

            String query = (sqlQueries != null) ? sqlQueries.getProperty("insert-addEmbarcacion") : null;
            if (query != null) {
                int result = jdbcTemplate.update(
                    query,
                    e.getMatricula(),
                    e.getNombre(),
                    e.getTipo().toString(),
                    e.getPlazas(),
                    e.getDimensiones(),
                    (e.getPatronAsignado() != null ? e.getPatronAsignado().getId() : null)
                );
                return result > 0;
            } else return false;
        } catch (DataAccessException ex) {
            System.err.println("Unable to insert embarcacion into the db");
            ex.printStackTrace();
            return false;
        }
    }
}
