package store;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ShoppingCartServlet
 */
@WebServlet("/ShoppingCartServlet")
public class ShoppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ShoppingCart sc = (ShoppingCart)session.getAttribute(ShoppingCart.SC_NAME);
		ProductCatalog pc = (ProductCatalog)request.getServletContext().getAttribute(ProductCatalog.PC_NAME);
		String id = request.getParameter("productID");
		if (id != null) {
			sc.addProduct(pc.getProduct(id), 1);
		} else {
			sc = new ShoppingCart();
			Enumeration<String> enumeration = request.getParameterNames();
		    while (enumeration.hasMoreElements()) {
		        id = enumeration.nextElement();
		        sc.addProduct(pc.getProduct(id), Integer.parseInt(request.getParameter(id)));
		    }
		    session.setAttribute(ShoppingCart.SC_NAME, sc);
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher("shopping-cart.jsp");
		dispatch.forward(request, response);
	}

}
