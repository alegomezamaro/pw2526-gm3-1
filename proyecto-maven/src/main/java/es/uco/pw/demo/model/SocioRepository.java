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
public class SocioRepository{

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public SocioRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName){
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    private void createProperties(){
        sqlQueries = new Properties();
        try{
            BufferedReader reader;
            File f = new File(sqlQueriesFileName);
            reader = new BufferedReader(new FileReader(f));
            sqlQueries.load(reader);
        }
        catch (IOException e){
            System.err.println("Error creating properties object for SQL Queries");
            e.printStackTrace();
        }
    }

    public List<Socio> findAllSocios(){
      try{
        String query = sqlQueries.getProperty("select-findAllSocios");
        if(query != null){

          List<Socio> socios = jdbcTemplate.query(query, new RowMapper<Socio>(){
            public Socio mapRow(ResultSet rs, int rowNumber) throws SQLException{
              return new Socio(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("surname"),
                Date.valueOf(rs.getString("birthdate")).toLocalDate(),
                Date.valueOf(rs.getString("inscriptiondate")).toLocalDate(),
                rs.getString("address"),
                rs.getBoolean("patronembarcacion"),
                rs.getInt("inscriptionid"),
                rs.getInt("familiaid"),
                FamiliaType.valueOf(rs.getString("relacionfamiliar"))
                );
              };
            });
            return socios;
        }
        else return null;
      }
      catch(DataAccessException e){
        System.err.println("Unable to find socios");
        e.printStackTrace();
        return null;
      }
    }

    public boolean addSocio(Socio socio){
      try{
        String query = sqlQueries.getProperty("insert-addSocio");
        if(query != null){

          int result = jdbcTemplate.update(query,
            socio.getDni(),
            socio.getName(),
            socio.getSurname(),
            socio.getBirthDate().toString(),
            socio.getInscriptionDate().toString(),
            socio.getAddress(),
            socio.isPatronEmbarcacion(),
            socio.getInscriptionId(),
            socio.getFamiliaId(),
            socio.getRelacionFamiliar()
          );
          if(result>0) return true;
          else return false;

        }
        else return false;
      }
      catch(DataAccessException e){
        System.err.println("Unable to insert socio into the db");
        e.printStackTrace();
        return false;
      }
    }

}