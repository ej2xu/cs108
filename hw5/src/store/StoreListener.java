package store;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import login.AccountManager;

/**
 * Application Lifecycle Listener implementation class StoreListener
 *
 */
@WebListener
public class StoreListener implements ServletContextListener, HttpSessionListener {

    /**
     * Default constructor. 
     */
    public StoreListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent event)  { 
         event.getSession().setAttribute(ShoppingCart.SC_NAME, new ShoppingCart());
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent event)  { 
    	event.getSession().removeAttribute(ShoppingCart.SC_NAME);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  {
        event.getServletContext().setAttribute(ProductCatalog.PC_NAME, new ProductCatalog());
    }
	
}
