package pe.com.prueba.plataformacontrolcomercioseguridad.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.ProducerDetailsDTO;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Producer;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.User;
import pe.com.prueba.plataformacontrolcomercioseguridad.repository.ProducerRepository;
import pe.com.prueba.plataformacontrolcomercioseguridad.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final UserRepository userRepository;

    public List<ProducerDetailsDTO> getAllProducers() {
        return producerRepository.findAll().stream()
                .map(this::mapToProducerDetailsDTO)
                .collect(Collectors.toList());
    }

    public List<ProducerDetailsDTO> getPendingProducers() {
        return producerRepository.findByApproved(false).stream()
                .map(this::mapToProducerDetailsDTO)
                .collect(Collectors.toList());
    }

    public ProducerDetailsDTO getProducerById(Long id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Productor no encontrado"));
        return mapToProducerDetailsDTO(producer);
    }

    public ProducerDetailsDTO getProducerByUserId(Long userId) {
        Producer producer = producerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Productor no encontrado"));
        return mapToProducerDetailsDTO(producer);
    }

    @Transactional
    public ProducerDetailsDTO approveProducer(Long producerId) {
        Producer producer = producerRepository.findById(producerId)
                .orElseThrow(() -> new RuntimeException("Productor no encontrado"));

        producer.setApproved(true);

        User user = producer.getUser();
        user.setVerified(true);

        userRepository.save(user);

        return mapToProducerDetailsDTO(producerRepository.save(producer));
    }

    private ProducerDetailsDTO mapToProducerDetailsDTO(Producer producer) {
        return ProducerDetailsDTO.builder()
                .id(producer.getId())
                .userId(producer.getUser().getId())
                .name(producer.getUser().getName())
                .email(producer.getUser().getEmail())
                .businessName(producer.getBusinessName())
                .description(producer.getDescription())
                .location(producer.getLocation())
                .phone(producer.getPhone())
                .approved(producer.isApproved())
                .documents(producer.getDocuments())
                .createdAt(producer.getCreatedAt())
                .build();
    }
}
