package es.uco.pw.demo.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class InscripcionRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
   private String sqlQueriesFileName = "db/sql.properties";

    public InscripcionRepository(JdbcTemplate jdbcTemplate) {
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

    public List<Inscripcion> findAllInscripciones() {
    try {
        String query = sqlQueries.getProperty("select-findAllInscripciones");
        if (query != null) {
            return jdbcTemplate.query(query, new RowMapper<Inscripcion>() {
                @Override
                public Inscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Inscripcion ins = new Inscripcion();
                    ins.setId(rs.getInt("id"));
                    String typeStr = rs.getString("tipoCuota");
                    if (typeStr != null) ins.setTipoCuota(InscripcionType.valueOf(typeStr));
                    ins.setCuotaAnual(rs.getInt("cuotaAnual"));
                    ins.setDniTitular(rs.getString("dniTitular"));
                    if (rs.getDate("fechaInscripcion") != null) ins.setFechaInscripcion(rs.getDate("fechaInscripcion").toLocalDate());
                    ins.setFamiliaId(rs.getInt("familiaId"));
                    return ins;
                }
            });
        } else {
            return Collections.emptyList();   // <— antes devolvía null
        }
    } catch (DataAccessException e) {
        System.err.println("Unable to find inscripciones");
        e.printStackTrace();
        return Collections.emptyList();       // <— antes devolvía null
    }
}

    public List<Inscripcion> findInscripcionesByTipo(InscripcionType tipo) {
        // Si no se pasa tipo, devolvemos todas
        if (tipo == null) {
            return findAllInscripciones();
        }

        return findAllInscripciones()
                .stream()
                .filter(i -> tipo.equals(i.getTipoCuota()))
                .collect(Collectors.toList());
    }

    public boolean addInscripcion(Inscripcion ins) {
        try {
            if (sqlQueries == null) createProperties();
            
            String query = sqlQueries.getProperty("insert-addInscripcion");
            if (query != null) {
                int result = jdbcTemplate.update(
                    query,
                    ins.getTipoCuota() != null ? ins.getTipoCuota().toString() : null,
                    ins.getCuotaAnual(),
                    ins.getDniTitular(),
                    ins.getFechaInscripcion() != null ? Date.valueOf(ins.getFechaInscripcion()) : null,
                    ins.getFamiliaId()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to insert inscripcion into the db");
            ex.printStackTrace();
            return false;
        }
    }

    // ===  buscar una inscripción por id ===
    public Inscripcion findInscripcionById(Integer id) {
        String sql = "SELECT * FROM Inscripcion WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Inscripcion ins = new Inscripcion();
                ins.setId(rs.getInt("id"));
                String typeStr = rs.getString("tipoCuota");
                if (typeStr != null) {
                    ins.setTipoCuota(InscripcionType.valueOf(typeStr));
                }
                ins.setCuotaAnual(rs.getInt("cuotaAnual"));
                ins.setDniTitular(rs.getString("dniTitular"));
                if (rs.getDate("fechaInscripcion") != null) {
                    ins.setFechaInscripcion(rs.getDate("fechaInscripcion").toLocalDate());
                }
                ins.setFamiliaId(rs.getInt("familiaId"));
                return ins;
            });
        } catch (DataAccessException e) {
            System.err.println("Unable to find inscripcion with id " + id);
            e.printStackTrace();
            return null;
        }
    }

    // buscar inscripcion por titular
    public boolean existsInscripcionByTitular(String dniTitular) {
        String sql = "SELECT COUNT(*) FROM Inscripcion WHERE dniTitular = ?";
        
        try {

            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, dniTitular);
            
            return count != null && count > 0;
            
        } catch (DataAccessException e) {
            System.err.println("Error checking existence for dni: " + dniTitular);
            e.printStackTrace();
            return false;
        }
    }

    // ===  actualizar inscripción completa ===
    public boolean updateInscripcion(Inscripcion ins) {
        String sql = "UPDATE Inscripcion " +
                     "SET tipoCuota = ?, cuotaAnual = ?, dniTitular = ?, " +
                     "fechaInscripcion = ?, familiaId = ? " +
                     "WHERE id = ?";
        try {
            int rows = jdbcTemplate.update(
                sql,
                ins.getTipoCuota() != null ? ins.getTipoCuota().toString() : "INDIVIDUAL",
                ins.getCuotaAnual(),
                ins.getDniTitular(),
                ins.getFechaInscripcion() != null ? Date.valueOf(ins.getFechaInscripcion()) : null,
                ins.getFamiliaId(),
                ins.getId()
            );
            return rows > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to update inscripcion in the db");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteInscripcionByDniTitular(String dniTitular) {
        String sql = "DELETE FROM Inscripcion WHERE dniTitular = ?";
        try {
            int rows = jdbcTemplate.update(sql, dniTitular);
            return rows > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to delete inscripcion in the db");
            return false;
        }
    }
}
