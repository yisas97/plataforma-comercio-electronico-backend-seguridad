package pe.com.prueba.plataformacontrolcomercioseguridad.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.ProducerDetailsDTO;
import pe.com.prueba.plataformacontrolcomercioseguridad.services.ProducerService;

import java.util.List;

@RestController
@RequestMapping("/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProducerDetailsDTO>> getAllProducers() {
        return ResponseEntity.ok(producerService.getAllProducers());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProducerDetailsDTO>> getPendingProducers() {
        return ResponseEntity.ok(producerService.getPendingProducers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @producerSecurity.isCurrentProducer(#id)")
    public ResponseEntity<ProducerDetailsDTO> getProducerById(@PathVariable Long id) {
        return ResponseEntity.ok(producerService.getProducerById(id));
    }

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#userId)")
    public ResponseEntity<ProducerDetailsDTO> getProducerByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(producerService.getProducerByUserId(userId));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProducerDetailsDTO> approveProducer(@PathVariable Long id) {
        return ResponseEntity.ok(producerService.approveProducer(id));
    }
}