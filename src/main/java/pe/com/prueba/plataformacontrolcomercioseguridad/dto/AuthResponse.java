package pe.com.prueba.plataformacontrolcomercioseguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private boolean verified;

    // Solo para productores
    private boolean approved;
}