package co.edu.sena.lionwarrior.controller;

import co.edu.sena.lionwarrior.dto.ProductRequest;
import co.edu.sena.lionwarrior.dto.ProductResponse;
import co.edu.sena.lionwarrior.service.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints del catálogo de productos de DistriLion. */
@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /api/products -> 200 con la lista
    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.findAll();
    }

    // GET /api/products/{id} -> 200, o 404
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Integer id) {
        return productService.findById(id);
    }

    // POST /api/products -> 201, 400 o 409
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    // PUT /api/products/{id} -> 200, 400, 404 o 409
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Integer id, @Valid @RequestBody ProductRequest request ) {
        return productService.update(id, request);
    }

    // DELETE /api/products/{id} -> 204, 404 o 409
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }


}