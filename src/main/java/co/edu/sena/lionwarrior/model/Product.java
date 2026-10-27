package co.edu.sena.lionwarrior.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/** Producto de la distribuidora (DistriLion). Tabla products. */

@Entity
@Table(name = "products")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Integer idProduct;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "product_description", length = 50)
    private String productDescription;

    @Column(name = "price_retail", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceRetail;

    @Column(name = "price_wholesale", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceWholesale;

    // Columna stock_actual de la BD
    @Column(name = "stock_actual", nullable = false)
    private Integer currentStock;

    // Columna stock_minimo de la BD
    @Column(name = "stock_minimo", nullable = false)
    private Integer minimumStock;

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
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

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(BigDecimal priceRetail) {
        this.priceRetail = priceRetail;
    }

    public BigDecimal getPriceWholesale() {
        return priceWholesale;
    }

    public void setPriceWholesale(BigDecimal priceWholesale) {
        this.priceWholesale = priceWholesale;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }
}
