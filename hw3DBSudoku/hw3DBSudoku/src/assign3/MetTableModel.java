package assign3;

import javax.swing.table.*;
import java.util.*;
import java.io.*;
import java.sql.*;

public class MetTableModel extends AbstractTableModel {
	private static String account = MyDBInfo.MYSQL_USERNAME;
	private static String password = MyDBInfo.MYSQL_PASSWORD;
	private static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	private static String database = MyDBInfo.MYSQL_DATABASE_NAME;
	
	private ResultSet rs;
	private Statement stmt;	
	private ResultSetMetaData rsmd;
	private String update;
	private String query;
	
	public MetTableModel() {
		rs = null;
		rsmd = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection
				( "jdbc:mysql://" + server, account ,password);			
			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getColumnName(int col) {
		String name = "";
		if (rsmd != null) {
	        try {
	            name = rsmd.getColumnName(col + 1);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
        return name;
	}

	public int getColumnCount() {
		int count = 0;
		if (rsmd != null) {
	        try {
	            count = rsmd.getColumnCount();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
		return count;
	}

	public int getRowCount() {
		int count = 0;
		if (rs != null) {
	        try {
	            rs.last();
	            count = rs.getRow();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
		return count;
	}

	public Object getValueAt(int row, int col) {
		Object result = null;
		if (rs != null) {
			try {
				rs.absolute(row + 1);
				result = rs.getObject(col + 1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void addColumn(String name) {
		fireTableStructureChanged();
	}

	public void addData(String city, String region, String popNum) {
		try {
			update = "INSERT INTO metropolises VALUES(\"" + city + "\",\"" + region + "\","+ popNum + ");";
			query = "SELECT * FROM metropolises WHERE metropolis = \""	+ city + "\" AND continent = \""
					+ region + "\" AND population = " + popNum + ";";
			stmt.executeUpdate(update);
			rs = stmt.executeQuery(query);
			rsmd = rs.getMetaData();
			fireTableStructureChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
}
