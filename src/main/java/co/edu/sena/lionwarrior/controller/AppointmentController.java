package co.edu.sena.lionwarrior.controller;

import co.edu.sena.lionwarrior.dto.AppointmentResponse;
import co.edu.sena.lionwarrior.dto.CreateAppointmentRequest;
import co.edu.sena.lionwarrior.dto.UpdateAppointmentStatusRequest;
import co.edu.sena.lionwarrior.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints de las citas. */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // GET /api/appointments, ?idUser=1 o ?idBarber=1 -> 200
    @GetMapping
    public List<AppointmentResponse> getAll(@RequestParam(required = false) Integer idUser,
                                            @RequestParam(required = false) Integer idBarber) {
        return appointmentService.findAll(idUser, idBarber);
    }

    // GET /api/appointments/{id} -> 200 o 404
    @GetMapping("/{id}")
    public AppointmentResponse getById(@PathVariable Integer id) {
        return appointmentService.findById(id);
    }

    // POST /api/appointments -> 201, 400, 404 o 409
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody CreateAppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(request));
    }

    // PATCH /api/appointments/{id}/status -> 200, 400, 404 o 409
    // PATCH (y no PUT) porque solo se cambia un campo de la cita
    @PatchMapping("/{id}/status")
    public AppointmentResponse updatestatus(@PathVariable Integer id,
                                            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        return appointmentService.updateStatus(id, request.status());
    }
}
