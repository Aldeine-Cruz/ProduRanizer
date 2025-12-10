
package tabletest;

/**
 *
 * @author john
 */
public class ProductTable {
    private String prodName;
    private String price;
    private int quantity;

    

    public String getProdName() {
        return prodName;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

   
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductTable(String prodName, String price, int quantity) {
        this.prodName = prodName;
        this.price = price;
        this.quantity = quantity;
    }
    
    
}
