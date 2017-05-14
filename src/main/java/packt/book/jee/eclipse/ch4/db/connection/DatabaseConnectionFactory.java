package packt.book.jee.eclipse.ch4.db.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * 
 * @author manolo
 *
 *	Singleton factory class para crear conexiones JDBC
 */

public class DatabaseConnectionFactory {

	//Instancia Singleton
	private static DatabaseConnectionFactory conFactory = new DatabaseConnectionFactory();
	
	private DataSource datasource = null;
	
	//creamos el constructor privado
	private DatabaseConnectionFactory() {
		
	}
	
	/**
	 * Debe de ser llamado antes que ningun otro metodo en esta clase.
	 * Inicializmos el datasource y lo salvamos en una variable instancia
	 */
	
	public synchronized void init() throws IOException {
		//Comprobamos si metodo init ya ha sido llamado
		if (datasource != null)
			return;
		//Cargamos el fichero db.properties (resources)
		InputStream inStream = this.getClass().getResourceAsStream("db.properties");
		Properties dbProperties = new Properties();
		dbProperties.load(inStream);
		inStream.close();
		
		
		//Creamos Pool Properties especifico de Tomcat
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:mysql://" + dbProperties.getProperty("db_host") + 
				":" + dbProperties.getProperty("db_port") + "/" +
				dbProperties.getProperty("db_name"));
		p.setDriverClassName(dbProperties.getProperty("db_driver_class_name"));
		p.setUsername(dbProperties.getProperty("db_user_name"));
		p.setPassword(dbProperties.getProperty("db_password"));
		p.setMaxActive(10);
		
		datasource = new DataSource();
		datasource.setPoolProperties(p);
		
	}
	
	//proveemos acceso a la instancia singleton
	public static DatabaseConnectionFactory getConnectionFactory() {
		return conFactory;
	}
	
	//devolvemos el objeto database connection
	public Connection getConnection () throws SQLException {
		if (datasource == null)
			throw new SQLException("Error inicializando datasource");
		return datasource.getConnection();
	}
	
}
