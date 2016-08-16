package store;

import java.math.BigDecimal;
import java.util.*;

public class ShoppingCart {
	public static final String SC_NAME = "Shopping Cart";
	public HashMap<Product, Integer> cart;
	public BigDecimal total;
	
	public ShoppingCart() {
		cart = new HashMap<Product, Integer>();
		total = new BigDecimal(0);
	}
	
	public void addProduct(Product p, int q) {
		if (q > 0) {
			if (cart.containsKey(p)) cart.put(p, cart.get(p) + q);
			else cart.put(p, q);
			total = total.add(p.price.multiply(new BigDecimal(q)));
		}
	}
}
