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
public class FamiliaRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public FamiliaRepository(JdbcTemplate jdbcTemplate) {
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

    public List<Familia> findAllFamilias() {
        try {
            String query = sqlQueries.getProperty("select-findAllFamilias");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Familia>() {
                    @Override
                    public Familia mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Familia f = new Familia();
                        f.setId(rs.getInt("id"));
                        f.setMainDni(rs.getInt("maindni"));
                        f.setFamiliaDnis(null);
                        return f;
                    }
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find familias");
            e.printStackTrace();
            return null;
        }
    }

    public boolean addFamilia(Familia f) {
        try {
            String query = sqlQueries.getProperty("insert-addFamilia");
            if (query != null) {
                int result = jdbcTemplate.update(
                    query,
                    f.getId(),
                    f.getMainDni()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to insert familia into the db");
            ex.printStackTrace();
            return false;
        }
    }
}
