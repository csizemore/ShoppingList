package project.shoppinglist;

/**
 * Created by Crystal on 11/14/2016.
 */

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Peter on 2015.07.01..
 */
public class Product extends SugarRecord<Product> implements Serializable {
    public enum ProductType {
        FOOD(0, R.drawable.food),
        CLOTHES(1, R.drawable.clothes), BOOKS(2, R.drawable.books);

        private int value;
        private int iconId;

        private ProductType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ProductType fromInt(int value) {
            for (ProductType p : ProductType.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return FOOD;
        }
    }

    private String productName;
    private String estimatedPrice;
    private String productDescription;
    private ProductType productType;
    private boolean checked;

    public Product() {

    }

    public Product(String productName, String productDescription, String estimatedPrice,
                   ProductType productType, boolean checked) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.estimatedPrice = estimatedPrice;
        this.productType = productType;
        this.checked = checked;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public String getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(String estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public ProductType getProductType() {return productType;}

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setChecked(boolean bool) {this.checked = bool;};

    public boolean getChecked() {return checked;}


}
