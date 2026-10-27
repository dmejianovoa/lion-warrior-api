package co.edu.sena.lionwarrior.controller;

import co.edu.sena.lionwarrior.dto.BarberResponse;
import co.edu.sena.lionwarrior.dto.CreateBarberRequest;
import co.edu.sena.lionwarrior.dto.UpdateBarberRequest;
import co.edu.sena.lionwarrior.service.BarberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints de los barberos. */
@RestController
@RequestMapping("/api/barbers")
public class BarberController {

    private final BarberService barberService;

    public BarberController(BarberService barberService) {
        this.barberService = barberService;
    }

    // GET /api/barbers  o  GET /api/barbers?active=true -> 200
    @GetMapping
    public List<BarberResponse> getAll(@RequestParam(required = false) Boolean active) {
        return barberService.findAll(active);
    }

    // GET /api/barbers/{id} -> 200, o 404
    @GetMapping("/{id}")
    public BarberResponse getById(@PathVariable Integer id) {
        return barberService.findById(id);
    }

    // POST /api/barbers -> 201, 400, 404 o 409
    @PostMapping
    public ResponseEntity<BarberResponse> create(@Valid @RequestBody CreateBarberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(barberService.create(request));
    }

    // PUT /api/barbers/{id} -> 200, 400 o 404
    @PutMapping("/{id}")
    public BarberResponse update(@PathVariable Integer id, @Valid @RequestBody UpdateBarberRequest request) {
        return barberService.update(id, request);
    }
}