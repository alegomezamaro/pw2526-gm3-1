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
public class ReservaRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName = "db/sql.properties";
    public ReservaRepository(JdbcTemplate jdbcTemplate) {
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

    public List<Reserva> findAllReservas() {
        try {
            if (sqlQueries == null) createProperties();

            String query = sqlQueries.getProperty("select-findAllReservas");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Reserva>() {
                    @Override
                    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Reserva(
                            rs.getInt("id"),
                            rs.getString("matriculaEmbarcacion"),
                            rs.getInt("plazasreserva"),
                            rs.getDate("fechareserva") != null
                                ? rs.getDate("fechareserva").toLocalDate()
                                : null,
                            rs.getDouble("precioreserva")
                        );
                    }
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find reservas");
            e.printStackTrace();
            return null;
        }
    }

    public boolean addReserva(Reserva r) {
        try {
            if (sqlQueries == null) createProperties();
            String query = sqlQueries.getProperty("insert-addReserva");
            if (query != null) {
                int result = jdbcTemplate.update(
                    query,
                    (r.getMatriculaEmbarcacion() != null ? r.getMatriculaEmbarcacion(): null),
                    r.getPlazasReserva(),
                    r.getFechaReserva() != null ? Date.valueOf(r.getFechaReserva()) : null,
                    r.getPrecioReserva()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to insert reserva into the db");
            ex.printStackTrace();
            return false;
        }
    }

    // Actualizar solo la fecha de una reserva
public boolean updateFechaReserva(int id, LocalDate nuevaFecha) {
    String sql = "UPDATE Reserva SET fechaReserva = ? WHERE id = ?";
    try {
        jdbcTemplate.update(
                sql,
                nuevaFecha != null ? Date.valueOf(nuevaFecha) : null,
                id
        );
        return true;
    } catch (DataAccessException ex) {
        System.err.println("Unable to update reserva date in the db");
        ex.printStackTrace();
        return false;
    }
}


}  