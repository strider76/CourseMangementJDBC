package packt.book.jee.eclipse.ch4.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import packt.book.jee.eclipse.ch4.bean.Course;
import packt.book.jee.eclipse.ch4.bean.Teacher;
import packt.book.jee.eclipse.ch4.db.connection.DatabaseConnectionFactory;

public class CourseDAO {

	public static void addCourse (Course course) throws SQLException {
		//obtenemos un Connection del Connection Pool de tomcat
		Connection con = DatabaseConnectionFactory.getConnectionFactory().getConnection();
		try {
			final String sql = "insert into Course (name, credits) values (?,?)";
			//creamos el preparecStatement con una opcion para optener la clave autogenerada
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//Asignamos los parametros
			stmt.setString(1, course.getName());
			stmt.setInt(2, course.getCredits());
			//Ejecutamos la consulta con executa devolviendonos boolean con resultado operacion
			stmt.execute();
			//Obtenemos el id autogenerado
			ResultSet rs = stmt.getGeneratedKeys();
			
			if (rs.next())
				course.setId(rs.getInt(1));
			rs.close();
			stmt.close();
			
		} finally {
			con.close();
		}
	}
	
	public List<Course> getCourses () throws SQLException {
		
		//Obtenemos la conexion desde el connection pool de tomcat
		
		Connection conn = DatabaseConnectionFactory.getConnectionFactory().getConnection();
		
		List<Course> courses = new ArrayList<Course>();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			
			//Creamos la consulta SQL utilizando un left outter join
			StringBuilder sb = new StringBuilder("select course.id as courseId, course.name as courseName,")
					.append("course.credits as credits, Teacher.id as teacherId, Teacher.first_name as firstName,")
					.append("Teacher.last_name as lastName, Teacher.designation as designation ")
					.append("from Course left outter join Teacher on ")
					.append("course.Teacher_id = Teacher.id ")
					.append("order by course.name");
			
			//Ejecutamos la consulta
			rs = stmt.executeQuery(sb.toString());
			
			//Iteramos sobre el ResultSet y creamos los objetos Course
			while (rs.next()) {
				Course course = new Course();
				course.setId(rs.getInt("courseId"));
				course.setName(rs.getString("courseName"));
				course.setCredits(rs.getInt("credits"));
				courses.add(course);
				
				int teacherId = rs.getInt("teacherId");
				//comprobamos si el teacherId es nulo en la tabla
				if (rs.wasNull()) // devuelve un booleano si la ultima columa leida ("teacherId") tiene un valor nulo o no
					continue; //Pasamos a la siguiente iteracion del ResultSet
				Teacher teacher = new Teacher();
				teacher.setId(teacherId);
				teacher.setFirstName(rs.getString("firstName"));
				teacher.setLastName(rs.getString("lastName"));
				teacher.setDesignation(rs.getString("designation"));
				course.setTeacher(teacher);
				
				
			}
			
			return courses;
			
		} finally {
			try {if (rs!=null) rs.close(); } catch (SQLException e) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e) {}
			try {conn.close();} catch (SQLException e) {}
		}
	}
}
