package pe.com.prueba.plataformacontrolcomercioseguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProducerDetailsDTO {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String businessName;
    private String description;
    private String location;
    private String phone;
    private boolean approved;
    private List<String> documents;
    private LocalDateTime createdAt;
}