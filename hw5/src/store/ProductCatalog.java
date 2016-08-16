package store;

import java.util.*;
import java.sql.*;

public class ProductCatalog {
	public static final String PC_NAME = "Product Catalog";
	private DBConnection connection;
	
	public ProductCatalog() {
		connection = new DBConnection();
	}
	
	public ResultSet getAllProducts() {
		ResultSet rs = null;
		try {
			rs = connection.stmt.executeQuery("SELECT * FROM products");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return rs;
	}
	
	public Product getProduct(String id) {
		Product result = null;
		try {
			ResultSet rs = connection.stmt.executeQuery("SELECT * FROM products WHERE productid =\""+id+"\";");
			if (rs.next()) {
				result = new Product(rs.getString("productid"), rs.getString("name"),
									rs.getString("imagefile"),rs.getBigDecimal("price"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
