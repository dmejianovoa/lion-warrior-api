package co.edu.sena.lionwarrior.controller;

import co.edu.sena.lionwarrior.dto.ServiceItemRequest;
import co.edu.sena.lionwarrior.dto.ServiceItemResponse;
import co.edu.sena.lionwarrior.service.ServiceItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints del catálogo de servicios de la barbería. */
@RestController
@RequestMapping("/api/services")
public class ServiceItemController {

    private final ServiceItemService serviceItemService;

    public ServiceItemController(ServiceItemService serviceItemService) {
        this.serviceItemService = serviceItemService;
    }

    // GET /api/services -> 200 con la lista
    @GetMapping
    public List<ServiceItemResponse> getAll() {
        return serviceItemService.findAll();
    }

    // GET /api/services/{id} -> 200, o 404
    @GetMapping("/{id}")
    public ServiceItemResponse getById(@PathVariable Integer id) {
        return serviceItemService.findById(id);
    }

    // POST /api/services -> 201, 400 o 409
    @PostMapping
    public ResponseEntity<ServiceItemResponse> create(@Valid @RequestBody ServiceItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceItemService.create(request));
    }

    // PUT /api/services/{id} -> 200, 400, 404 o 409
    @PutMapping("/{id}")
    public ServiceItemResponse update(@PathVariable Integer id, @Valid @RequestBody ServiceItemRequest request) {
        return serviceItemService.update(id, request);
    }

    // DELETE /api/services/{id} -> 204, 404 o 409
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        serviceItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}