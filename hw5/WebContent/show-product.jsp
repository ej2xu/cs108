<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="store.ProductCatalog, store.Product"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
ProductCatalog pc = (ProductCatalog) application.getAttribute(ProductCatalog.PC_NAME);
Product product = pc.getProduct(request.getParameter("id"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><%=product.name%></title>
</head>
<body>
	<h1><%=product.name%></h1>
	<img alt="<%=product.name%>" src="<%="/hw5/store-images/" + product.image%>" />
	
	<form action="ShoppingCartServlet" method="post">
	    $<%=product.price%> <input name="productID" type="hidden" value="<%=product.id%>"/>
	    <input type="submit" value="Add to Cart"/>
	</form>	
</body>
</html>