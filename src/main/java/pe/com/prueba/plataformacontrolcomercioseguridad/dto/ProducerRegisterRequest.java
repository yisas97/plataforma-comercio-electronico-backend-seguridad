package pe.com.prueba.plataformacontrolcomercioseguridad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProducerRegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(min = 2, message = "El nombre del negocio debe tener al menos 2 caracteres")
    private String businessName;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")
    private String description;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(min = 5, message = "La ubicación debe tener al menos 5 caracteres")
    private String location;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 8, message = "El teléfono debe tener al menos 8 caracteres")
    private String phone;
}