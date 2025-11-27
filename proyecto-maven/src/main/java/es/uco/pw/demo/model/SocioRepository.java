package es.uco.pw.demo.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SocioRepository {

    private final JdbcTemplate jdbcTemplate;

    public SocioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Método para añadir un socio
    public boolean addSocio(Socio socio) {
        String query = "INSERT INTO Socio (dni, nombre, apellidos, direccion, fechaNacimiento, patronEmbarcacion, fechaAlta) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(query, 
                socio.getDni(), 
                socio.getNombre(),
                socio.getApellidos(), 
                socio.getDireccion(),
                socio.getFechaNacimiento(),
                socio.esPatronEmbarcacion(),
                socio.getFechaAlta()
            );
            return true;
        } catch (DataAccessException e) {
            System.err.println("Unable to insert socio into the db");
            e.printStackTrace();
            return false;
        }
    }

    // Método para encontrar un socio por DNI
    public Socio findSocioByDni(String dni) {
        String query = "SELECT * FROM Socio WHERE dni = ?";
        
        // RowMapper manual para MySQL 5.1.49
        List<Socio> socios = jdbcTemplate.query(query, new Object[]{dni}, new RowMapper<Socio>() {
            public Socio mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                return new Socio(
                    rs.getString("dni"),
                    rs.getString("nombre"),  // Usamos 'nombre' en lugar de 'name'
                    rs.getString("apellidos"),  // Usamos 'apellidos' en lugar de 'surname'
                    LocalDate.parse(rs.getString("fechaNacimiento")), // Convertir a LocalDate
                    rs.getString("direccion"),
                    rs.getBoolean("patronEmbarcacion"),
                    LocalDate.parse(rs.getString("fechaAlta")) // Convertir a LocalDate
                );
            }
        });

        // Si hay resultados, devolver el primer socio encontrado
        return socios.isEmpty() ? null : socios.get(0);
    }

    // Método para listar todos los socios
    public List<Socio> findAllSocios() {
        String query = "SELECT * FROM Socio";
        
        // RowMapper manual para MySQL 5.1.49
        return jdbcTemplate.query(query, new RowMapper<Socio>() {
            public Socio mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                return new Socio(
                    rs.getString("dni"),
                    rs.getString("nombre"),  // Usamos 'nombre' en lugar de 'name'
                    rs.getString("apellidos"),  // Usamos 'apellidos' en lugar de 'surname'
                    LocalDate.parse(rs.getString("fechaNacimiento")), // Convertir a LocalDate
                    rs.getString("direccion"),
                    rs.getBoolean("patronEmbarcacion"),
                    LocalDate.parse(rs.getString("fechaAlta"))
                );
            }
        });
    }

    // Método para actualizar los datos de un socio
    public boolean updateSocio(Socio socio) {
        String query = "UPDATE Socio SET nombre = ?, apellidos = ?, direccion = ?, fechaNacimiento = ?, fechaAlta = ?, patronEmbarcacion = ? WHERE dni = ?";
        try {
            jdbcTemplate.update(query,
                socio.getNombre(),  // Usamos 'nombre' en lugar de 'name'
                socio.getApellidos(),  // Usamos 'apellidos' en lugar de 'surname'
                socio.getDireccion(),
                socio.getFechaNacimiento(),
                socio.getFechaAlta(),
                socio.esPatronEmbarcacion(),
                socio.getDni()
            );
            return true;
        } catch (DataAccessException e) {
            System.err.println("Unable to update socio in the db");
            e.printStackTrace();
            return false;
        }
    }

    
}
