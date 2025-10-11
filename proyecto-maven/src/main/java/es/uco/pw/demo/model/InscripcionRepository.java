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
public class InscripcionRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

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
            BufferedReader reader;
            File f = new File(sqlQueriesFileName);
            reader = new BufferedReader(new FileReader(f));
            sqlQueries.load(reader);
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
                        String typeStr = rs.getString("type");
                        if (typeStr != null) {
                            ins.setType(InscriptionType.valueOf(typeStr));
                        }
                        ins.setYearFee(rs.getInt("yearfee"));
                        ins.setSocioTitular(rs.getInt("sociotitular"));
                        if (rs.getDate("date") != null) {
                            ins.setDate(rs.getDate("date").toLocalDate());
                        }
                        ins.setFamiliaId(rs.getInt("familiaid"));
                        return ins;
                    }
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find inscripciones");
            e.printStackTrace();
            return null;
        }
    }

    public boolean addInscripcion(Inscripcion ins) {
        try {
            String query = sqlQueries.getProperty("insert-addInscripcion");
            if (query != null) {
                int result = jdbcTemplate.update(
                    query,
                    ins.getId(),
                    ins.getType() != null ? ins.getType().toString() : null,
                    ins.getYearFee(),
                    ins.getSocioTitular(),
                    ins.getDate() != null ? Date.valueOf(ins.getDate()) : null,
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
}
