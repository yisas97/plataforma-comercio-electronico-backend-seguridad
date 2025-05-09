package pe.com.prueba.plataformacontrolcomercioseguridad.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);
    private final ProducerService producerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProducerDetailsDTO>> getAllProducers() {
        logger.info("Solicitando lista de todos los productores");
        List<ProducerDetailsDTO> producers = producerService.getAllProducers();
        logger.info("Se encontraron {} productores en total", producers.size());
        return ResponseEntity.ok(producers);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProducerDetailsDTO>> getPendingProducers() {
        logger.info("Solicitando lista de productores pendientes de aprobación");
        List<ProducerDetailsDTO> pendingProducers = producerService.getPendingProducers();
        logger.info("Se encontraron {} productores pendientes de aprobación", pendingProducers.size());
        return ResponseEntity.ok(pendingProducers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @producerSecurity.isCurrentProducer(#id)")
    public ResponseEntity<ProducerDetailsDTO> getProducerById(@PathVariable Long id) {
        logger.info("Solicitando información del productor con ID: {}", id);
        try {
            ProducerDetailsDTO producer = producerService.getProducerById(id);
            logger.info("Información del productor con ID {} obtenida exitosamente", id);
            return ResponseEntity.ok(producer);
        } catch (Exception e) {
            logger.error("Error al obtener productor con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#userId)")
    public ResponseEntity<ProducerDetailsDTO> getProducerByUserId(@PathVariable Long userId) {
        logger.info("Buscando productor asociado al usuario con ID: {}", userId);
        try {
            ProducerDetailsDTO producer = producerService.getProducerByUserId(userId);
            logger.info("Productor encontrado para el usuario con ID {}", userId);
            return ResponseEntity.ok(producer);
        } catch (Exception e) {
            logger.error("Error al buscar productor para usuario con ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProducerDetailsDTO> approveProducer(@PathVariable Long id) {
        logger.info("Iniciando aprobación del productor con ID: {}", id);
        try {
            ProducerDetailsDTO approvedProducer = producerService.approveProducer(id);
            logger.info("Productor con ID {} aprobado exitosamente", id);
            return ResponseEntity.ok(approvedProducer);
        } catch (Exception e) {
            logger.error("Error al aprobar productor con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}