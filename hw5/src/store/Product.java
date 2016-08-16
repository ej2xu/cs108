package store;

import java.math.BigDecimal;

public class Product {
	public String id;
	public String name;
	public String image;
	public BigDecimal price;
	
	public Product(String i, String n, String im, BigDecimal p) {
		id = i;
		name = n;
		image = im;
		price = p;
	}
	
	@Override
    public int hashCode() {
        return id.hashCode();
    }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Product other = (Product) obj;
		if (id.equals(other.id)) return true;
		return false;
	}
}
