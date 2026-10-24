package co.edu.sena.lionwarrior.service;


import co.edu.sena.lionwarrior.dto.ServiceItemRequest;
import co.edu.sena.lionwarrior.dto.ServiceItemResponse;
import co.edu.sena.lionwarrior.model.ServiceItem;
import co.edu.sena.lionwarrior.repository.ServiceItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Lógica de negocio del catálogo de servicios. */
@Service
public class ServiceItemService {

    private final ServiceItemRepository repository;

    public ServiceItemService(ServiceItemRepository repository) {
        this.repository = repository;
    }

    public List<ServiceItemResponse> findAll() {
        return repository.findAll().stream().map(ServiceItemResponse::from).toList();
    }

    public ServiceItemResponse findById(Integer id) {
        return ServiceItemResponse.from(getOrThrow(id));
    }

    /** Crea un servicio. No permite dos servicios con el mismo nombre. */
    public ServiceItemResponse create(ServiceItemRequest request) {
        if (repository.existsByServiceNameIgnoreCase(request.serviceName())) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un servicio con ese nombre");
        }
        ServiceItem item = new ServiceItem();
        copyData(item, request);
        return ServiceItemResponse.from(repository.save(item));
    }

    /** Reemplaza los datos del servicio. El nombre no puede ser el de otro servicio. */
    public ServiceItemResponse update(Integer id, ServiceItemRequest request) {
        ServiceItem item = getOrThrow(id);
        if(repository.existsByServiceNameIgnoreCaseAndIdServiceNot(request.serviceName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nombre previamente registrado");
        }
        copyData(item, request);
        return ServiceItemResponse.from(repository.save(item));
    }

    /** Elimina un servicio. Si ya está en alguna cita, la BD lo impide y se responde 409. */
    public void delete(Integer id) {
        repository.delete(getOrThrow(id));
    }

    // Busca el servicio o responde 404
    private ServiceItem getOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Servicio no encontrado con el id: " + id
                ));
    }

    // Pasa los datos del request a la entidad
    private void copyData(ServiceItem item, ServiceItemRequest request) {
        item.setServiceName(request.serviceName());
        item.setServiceDescription(request.serviceDescription());
        item.setServicePrice(request.servicePrice());
        item.setDurationMin(request.durationMin());
    }
}
