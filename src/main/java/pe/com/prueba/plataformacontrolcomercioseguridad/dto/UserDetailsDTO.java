package pe.com.prueba.plataformacontrolcomercioseguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Role;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean verified;
    private LocalDateTime createdAt;
}
