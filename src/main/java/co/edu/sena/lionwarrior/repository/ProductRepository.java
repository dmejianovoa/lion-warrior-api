package co.edu.sena.lionwarrior.repository;

import co.edu.sena.lionwarrior.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acceso a la tabla products. */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByProductNameIgnoreCase(String productName);

    boolean existsByproductNameIgnoreCaseAndIdProductNot(String productName, Integer idProduct);

}
