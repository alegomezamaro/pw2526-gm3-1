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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.sql.Date; 

@Repository
public class EmbarcacionRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName = "db/sql.properties";

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

    public List<Embarcacion> findAllEmbarcaciones() {
        try {
            // ✅ Carga perezosa para evitar NullPointerException
            if (sqlQueries == null) createProperties();

            String query = (sqlQueries != null) ? sqlQueries.getProperty("select-findAllEmbarcaciones") : null;
            if (query != null) {
                List<Embarcacion> embarcaciones = jdbcTemplate.query(query, new RowMapper<Embarcacion>() {
                    @Override
                    public Embarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Patron patron = null;
                        Integer patronId = rs.getObject("p_id", Integer.class);

                        if (patronId != null) {
                            patron = new Patron(
                                patronId,
                                rs.getString("p_dni"),
                                rs.getString("p_nombre"),
                                rs.getString("p_apellidos"),
                                rs.getDate("p_fechanac") != null ? rs.getDate("p_fechanac").toLocalDate() : null,
                                rs.getDate("p_fechatitulo") != null ? rs.getDate("p_fechatitulo").toLocalDate() : null
                            );
                        }
                        return new Embarcacion(
                            rs.getString("matricula"),
                            rs.getString("nombre"),
                            EmbarcacionType.valueOf(rs.getString("tipo")),
                            rs.getInt("plazas"),
                            rs.getString("dimensiones"),
                            rs.getInt("patronAsignado")
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
                    e.getPatronAsignado()
                );
                return result > 0;
            } else return false;
        } catch (DataAccessException ex) {
            System.err.println("Unable to insert embarcacion into the db");
            ex.printStackTrace();
            return false;
        }
    }

    public List<Embarcacion> findEmbarcacionesDisponibles(LocalDate fecha, int plazasNecesarias) {
        try {
            if (sqlQueries == null) {
                createProperties();
            }

            String query = sqlQueries.getProperty("select-findEmbarcacionesDisponibles");

            if (query == null) {
                System.err.println("SQL query 'select-findEmbarcacionesDisponibles' not found.");
                return List.of();
            }

            Date sqlDate = Date.valueOf(fecha);

            return jdbcTemplate.query(
                    query,
                    new Object[]{plazasNecesarias, sqlDate, sqlDate},
                    new RowMapper<Embarcacion>() {
                        @Override
                        public Embarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return new Embarcacion(
                                    rs.getString("matricula"),
                                    rs.getString("nombre"),
                                    EmbarcacionType.valueOf(rs.getString("tipo")),
                                    rs.getInt("plazas"),
                                    rs.getString("dimensiones"),
                                    rs.getInt("patronAsignado")
                            );
                        }
                    }
            );

        } catch (DataAccessException e) {
            System.err.println("Unable to find available embarcaciones");
            e.printStackTrace();
            return List.of();
        }
    }


    public boolean updatePatronAsignado(String matricula, Integer patronId) {
        try {
            if (sqlQueries == null) createProperties();

            String query = sqlQueries.getProperty("update-updatePatron");
            if (query != null) {
                // El patronId puede ser null (para desasignar)
                int result = jdbcTemplate.update(query, patronId, matricula);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            System.err.println("Unable to update patron for embarcacion " + matricula);
            ex.printStackTrace();
            return false;
        }
    }

    //actualizar embarcacion 

    public boolean updateEmbarcacion(Embarcacion e) {
    try {
        if (sqlQueries == null) createProperties();

        String query = sqlQueries.getProperty("update-updateEmbarcacion");
        if (query == null) {
            System.err.println("SQL query 'update-updateEmbarcacion' not found.");
            return false;
        }

        // ⚠ Si patronAsignado es 0, lo enviamos como NULL para que no rompa la FK
        Integer patron = e.getPatronAsignado();
        if (patron != null && patron == 0) {
            patron = null;
        }

        jdbcTemplate.update(
                query,
                e.getNombre(),
                e.getTipo() != null ? e.getTipo().toString() : null,
                e.getPlazas(),
                e.getDimensiones(),
                patron,              // <- usamos patron ya normalizado
                e.getMatricula()
        );

        return true;

        } catch (DataAccessException ex) {
            System.err.println("Unable to update embarcacion in the db");
            ex.printStackTrace();
            return false;
        }
    }

    public List<Integer> getAsignedPatrones(){
        String query = sqlQueries.getProperty("select-getAllPatrones");
        return jdbcTemplate.queryForList(query, Integer.class);
    }

}


