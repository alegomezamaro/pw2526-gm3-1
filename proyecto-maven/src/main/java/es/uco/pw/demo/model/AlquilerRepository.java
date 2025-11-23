package es.uco.pw.demo.model;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import es.uco.pw.demo.model.EmbarcacionType;
import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;

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
            
            InputStream is = getClass().getClassLoader().getResourceAsStream(sqlQueriesFileName);

            if (is == null) {
                System.err.println("ERROR: El fichero de properties NO se encontró en el classpath: " + sqlQueriesFileName);
                return;
            }

            
            sqlQueries.load(new InputStreamReader(is, StandardCharsets.UTF_8));
            is.close();
            
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

    public List<Alquiler> findAlquilerByEmbarcacionType(EmbarcacionType emb) {
        try {

            String query = sqlQueries.getProperty("select-findAlquilerByEmbarcacionType");
            
            String tipoBarco = emb.toString(); 

            return jdbcTemplate.query(query, new RowMapper<Alquiler>() {
                @Override
                public Alquiler mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Alquiler(
                        rs.getInt("id"),
                        rs.getString("matriculaEmbarcacion"),             
                        rs.getString("dniTitular"),
                        rs.getDate("fechaInicio") != null ? rs.getDate("fechaInicio").toLocalDate() : null,
                        rs.getDate("fechaFin") != null ? rs.getDate("fechaFin").toLocalDate() : null,
                        rs.getInt("plazasSolicitadas"),
                        rs.getDouble("precioTotal")
                    );
                }
            }, tipoBarco);

        } catch (DataAccessException e) {
            System.err.println("Unable to find alquileres for type: " + emb);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Alquiler findAlquilerById(Integer id) {
        if (sqlQueries == null) {
            createProperties();
        }
        String query = (sqlQueries != null) ? sqlQueries.getProperty("select-findAlquilerById") : null;
        try {
            return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
                Alquiler alquiler = new Alquiler();
                alquiler.setId(rs.getInt("id"));
                alquiler.setMatriculaEmbarcacion(rs.getString("matriculaEmbarcacion"));
                alquiler.setDniTitular(rs.getString("dniTitular"));
                java.sql.Date fechaInicio = rs.getDate("fechaInicio");
                if (fechaInicio != null) {
                    alquiler.setFechaInicio(fechaInicio.toLocalDate());
                }
                java.sql.Date fechaFin = rs.getDate("fechaFin");
                if (fechaFin != null) {
                    alquiler.setFechaFin(fechaFin.toLocalDate());
                }
                alquiler.setPlazasSolicitadas(rs.getInt("plazasSolicitadas"));
                alquiler.setPrecioTotal(rs.getDouble("precioTotal"));
                
                return alquiler;
            }, id);

        } catch (EmptyResultDataAccessException e) {
            System.out.println("No se encontró ningún alquiler con ID: " + id);
            return null;
        } catch (DataAccessException e) {
            System.err.println("Error al buscar alquiler con ID: " + id);
            e.printStackTrace();
            return null;
        }
    }
}

