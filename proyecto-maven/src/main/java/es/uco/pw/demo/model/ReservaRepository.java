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
public class ReservaRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName = "src/main/resources/db/sql.properties";
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
            BufferedReader reader;
            File f = new File(sqlQueriesFileName);
            reader = new BufferedReader(new FileReader(f));
            sqlQueries.load(reader);
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
                            /* embarcacion */ null,            // relación no hidratada
                            rs.getString("descripcion"),
                            /* socioSolicitante */ null,       // relación no hidratada
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
                    r.getId(),
                    // FK simples por ID/clave natural:
                    (r.getEmbarcacion() != null ? r.getEmbarcacion().getMatricula() : null),
                    r.getDescripcion(),
                    (r.getSocioSolicitante() != null ? r.getSocioSolicitante().getDni() : null),
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
}
