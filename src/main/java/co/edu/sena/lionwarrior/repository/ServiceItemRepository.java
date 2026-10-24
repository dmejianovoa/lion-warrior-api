package co.edu.sena.lionwarrior.repository;


import co.edu.sena.lionwarrior.model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acceso a la tabla services. */
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Integer> {

    // Boolean (Si existe un servicio con ese nombre) | No importa mayuscula | Uso -> creación
    boolean existsByServiceNameIgnoreCase(String serviceName);

    //Boolean (Mismo proceso, solo que ignora el propio servicio | Uso -> actualizar servicio)
    boolean existsByServiceNameIgnoreCaseAndIdServiceNot(String serviceName, Integer idService);
}
