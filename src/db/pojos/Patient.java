package db.pojos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Patient {
	
	public static void main(String args[])
	{
		try {
			Class.forName("org.sqlite.JDBC");
			Connection c= DriverManager.getConnection("jdbc:sqlite:Databasestep5.db");
			//c.createStatement().execute();
			Statement stmt=c.createStatement();
			String query= "INSERT INTO medsupply (id, name, quantity) VALUES(44, 'hol', 7.0)";
			stmt.executeUpdate(query);
			stmt.close();
			System.out.println();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("no tan guassa");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("guasaaa");
			e.printStackTrace();
		}
	}
}
