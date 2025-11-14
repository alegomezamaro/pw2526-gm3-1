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
public class AlquilerRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public AlquilerRepository(JdbcTemplate jdbcTemplate) {
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

    public List<Alquiler> findAllAlquileres() {
        try {
            String query = sqlQueries.getProperty("select-findAllAlquileres");
                return jdbcTemplate.query(query, new RowMapper<Alquiler>() {
                    @Override
                    public Alquiler mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Alquiler(
                            rs.getInt("id"),
                            rs.getString("matriculaEmbarcacion"),             
                            rs.getString("dniTitular"),
                            rs.getDate("fechaInicio") != null
                                ? rs.getDate("fechaInicio").toLocalDate()
                                : null,
                            rs.getDate("fechaFin") != null
                                ? rs.getDate("fechafin").toLocalDate()
                                : null,
                            rs.getInt("plazasSolicitadas"),
                            rs.getDouble("precioTotal")
                        );
                    }
                });
        } catch (DataAccessException e) {
            System.err.println("Unable to find alquileres");
            e.printStackTrace();
            return null;
        }
    }

    public boolean addAlquiler(Alquiler a) {
        try {
            String query = sqlQueries.getProperty("insert-addAlquiler");
            if (query != null) {
                int result = jdbcTemplate.update(
                    query,
                    a.getId(),
                    a.getMatriculaEmbarcacion(),
                    a.getDniTitular(),
                    a.getFechaInicio() != null ? Date.valueOf(a.getFechaInicio()) : null,
                    a.getFechaFin() != null ? Date.valueOf(a.getFechaFin()) : null,
                    a.getPlazasSolicitadas(),
                    a.getPrecioTotal()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to insert alquiler into the db");
            ex.printStackTrace();
            return false;
        }
    }
}

