<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="store.ProductCatalog, java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Student Store</title>
</head>
<body>
	<h1>Student Store</h1>
	<p>Item available:</p>

	<ul>
		<%
		ProductCatalog pc = (ProductCatalog) application.getAttribute(ProductCatalog.PC_NAME);
		ResultSet rs = pc.getAllProducts();
		String id, name;
		while (rs.next()) {
			id = rs.getString("productid");
			name = rs.getString("name");
			out.println("<li><a href=\"show-product.jsp?id=" + id + "\">" + name + "</a></li>");
		}
		%>
	</ul>
</body>
</html>