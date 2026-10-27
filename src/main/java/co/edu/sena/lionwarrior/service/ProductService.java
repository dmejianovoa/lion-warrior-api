package co.edu.sena.lionwarrior.service;

import co.edu.sena.lionwarrior.dto.ProductRequest;
import co.edu.sena.lionwarrior.dto.ProductResponse;
import co.edu.sena.lionwarrior.model.Product;
import co.edu.sena.lionwarrior.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Lógica de negocio del catálogo de productos. */
@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductResponse> findAll() {
        return repository.findAll().stream().map(ProductResponse::from).toList();
    }

    public ProductResponse findById(Integer id) {
        return ProductResponse.from(getOrThrow(id));
    }

    /**
     * Crea un producto. Nombre único y precio mayorista no mayor al precio al detal.
     */
    public ProductResponse create(ProductRequest request) {
        validateWholesalePrice(request);
        if (repository.existsByProductNameIgnoreCase(request.productName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese nombre");
        }
        Product product = new Product();
        copyData(product, request);
        return ProductResponse.from(repository.save(product));
    }

    /** Reemplaza los datos del producto con las mismas reglas del registro. */
    public ProductResponse update(Integer id, ProductRequest request) {
        Product product = getOrThrow(id);
        validateWholesalePrice(request);
        if (repository.existsByproductNameIgnoreCaseAndIdProductNot(request.productName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese nombre");
        }
        copyData(product, request);
        return ProductResponse.from(repository.save(product));
    }

    /** Elimina un producto. Si tiene ventas o reservas, la BD lo impide y se responde 409. */
    public void delete(Integer id) {
        repository.delete(getOrThrow(id));
    }

    // Regla de negocio: el precio mayorista no puede superar al del detal.
    // Con BigDecimal se compara con compareTo, no con ">"
    private void validateWholesalePrice(ProductRequest request) {
        if (request.priceWholesale().compareTo(request.priceRetail()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Bad request: El precio mayorista no puede ser mayor al precio al detal");
        }
    }

    private Product getOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con el id" + id
                ));
    }

    private void copyData(Product product, ProductRequest request) {
        product.setProductName(request.productName());
        product.setProductDescription(request.productDescription());
        product.setPriceRetail(request.priceRetail());
        product.setPriceWholesale(request.priceWholesale());
        product.setCurrentStock(request.currentStock());
        product.setMinimumStock(request.minimumStock());
    }
}
