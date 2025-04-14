package pe.com.prueba.plataformacontrolcomercioseguridad.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Producer;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.User;
import pe.com.prueba.plataformacontrolcomercioseguridad.repository.ProducerRepository;

import java.util.Optional;

@Component("producerSecurity")
@RequiredArgsConstructor
public class ProducerSecurity {

    private final ProducerRepository producerRepository;

    /**
     * Comprueba si el usuario autenticado es el productor solicitado
     */
    public boolean isCurrentProducer(Long producerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            return false;
        }

        Optional<Producer> producer = producerRepository.findById(producerId);

        return producer.isPresent() && producer.get().getUser().getId().equals(user.getId());
    }
}