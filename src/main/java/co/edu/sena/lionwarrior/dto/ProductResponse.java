package co.edu.sena.lionwarrior.dto;

import co.edu.sena.lionwarrior.model.Product;

import java.math.BigDecimal;

/** Producto tal como lo ve el cliente. */
public record ProductResponse(
        Integer idProduct,
        String productName,
        String productDescription,
        BigDecimal priceRetail,
        BigDecimal priceWholesale,
        Integer currentStock,
        Integer minimumStock
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getIdProduct(), product.getProductName(),
                product.getProductDescription(), product.getPriceRetail(), product.getPriceWholesale(),
                product.getCurrentStock(), product.getMinimumStock());
    }
}
