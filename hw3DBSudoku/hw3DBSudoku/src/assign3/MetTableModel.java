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
	
	private List<String> colNames;	// defines the number of cols
	private List<List> data;	// one List for each row
	
	
	public MetTableModel() {
		rs = null;
		colNames = new ArrayList<String>();
		data = new ArrayList<List>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection
				( "jdbc:mysql://" + server, account ,password);			
			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
			
// Sample Loop Access			
//			while(rs.next()) {
//				String s = rs.getString("metropolis");
//				int i = rs.getInt("population");
//				System.out.println(s + "\t" + i);
//			}
			con.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getColumnName(int col) {
		return colNames.get(col);
	}

	public int getColumnCount() {
		return(colNames.size());
	}

	public int getRowCount() {
		return(data.size());
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
		return(result);
	}

	public void addColumn(String name) {
		colNames.add(name);
		fireTableStructureChanged();
		/*
		 At present, TableModelListener does not have a more specific
		 notification for changing the number of columns.
		*/
	}

	public void addData(String city, String region, String popNum) {
		try {
			stmt.executeUpdate("INSERT INTO metropolises VALUES(\"" + city + "\",\""
								+ region + "\","+ popNum + ");");
			rs = stmt.executeQuery("SELECT * FROM metropolises WHERE metropolis = \""
								+ city + "\" AND continent = \"" + region + "\" AND population = "
								+ popNum + ";");
			fireTableRowsInserted(0, 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
