import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
	private final String stringConexion = "jdbc:oracle:thin:@localhost:1521:xe";
	
	//Metodo para acceder a la BD
	private Connection obtenerConexion(){
		Connection conexion = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conexion = DriverManager.getConnection(stringConexion, "estudiante","estudiante");
		} catch (ClassNotFoundException e) {
			System.out.println("No se encuentra el driver");
		}
		catch(SQLException e){
			System.out.println("Error en la conexión");
		}
		return conexion;
	}

	public static void main(String[] args) {
		if(new ConexionBD().obtenerConexion()!=null){
			System.out.println("Conexión exitosa");
		}
		else{
			System.out.println("Conexión fallida");
		}
	}

}
