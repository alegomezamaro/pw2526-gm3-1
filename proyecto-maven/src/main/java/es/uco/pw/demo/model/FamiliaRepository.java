package es.uco.pw.demo.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FamiliaRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;

    
    private String sqlQueriesFileName = "db/sql.properties";

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

    public List<Familia> findAllFamilias() {
        try {
           
            if (sqlQueries == null) createProperties();

            String query = (sqlQueries != null) ? sqlQueries.getProperty("select-findAllFamilias") : null;
            if( query == null ){
                return Collections.emptyList();
            }
            return jdbcTemplate.query(query, new RowMapper<Familia>() {
                @Override
                public Familia mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Familia f = new Familia();
                    f.setId(rs.getInt("id"));
                    f.setDniTitular(rs.getString("dniTitular"));
                    f.setNumAdultos(rs.getInt("numAdultos"));
                    f.setNumNiños(rs.getInt("numNiños"));
                    return f;
                }
            });
        } catch (DataAccessException e) {
            System.err.println("Unable to find familias");
            e.printStackTrace();
            return null;
        }
    }

    public void addFamilia(Familia familia) {
        String query = sqlQueries.getProperty("insert-addFamilia");
 
            jdbcTemplate.update(query, 
                familia.getDniTitular(),
                familia.getNumAdultos(), 
                familia.getNumNiños()
            );
    }

    public Familia findFamiliaById(int id) {
        try {
            if (sqlQueries == null) createProperties();
            String query = (sqlQueries != null) ? sqlQueries.getProperty("select-findFamiliaById") : null;
            if (query == null) {
                query = "SELECT id, dniTitular, numAdultos, `numNiños` FROM Familia WHERE id = ?";
            }
            return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
                Familia f = new Familia();
                f.setId(rs.getInt("id"));
                f.setDniTitular(rs.getString("dniTitular"));
                f.setNumAdultos(rs.getInt("numAdultos"));
                f.setNumNiños(rs.getInt("numNiños"));
                return f;
            }, id);
        } catch (DataAccessException e) {
            System.err.println("No se ha encontrado a la Familia con ID: " + id);
            e.printStackTrace();
            return null;
        }
    }
    
    public Integer getLastId(){
        String query = "SELECT LAST_INSERTED_ID()";
        try{
            return jdbcTemplate.queryForObject(query,Integer.class);
        }catch ( DataAccessException e){
            System.err.println("Unable to get last inserted ID");
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateFamilia(Familia familia){
        String query = sqlQueries.getProperty("update-updateFamilia");
        try{
            jdbcTemplate.update(query,
                familia.getNumAdultos(),
                familia.getNumNiños(),
                familia.getId()
            );
            return true;
        }catch ( DataAccessException e ){
            System.err.println("Unable to update familia in the db");
            e.printStackTrace();
            return false;
        }
    }

}
