package pe.com.prueba.plataformacontrolcomercioseguridad.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.AuthResponse;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.LoginRequest;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.ProducerRegisterRequest;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.RegisterRequest;
import pe.com.prueba.plataformacontrolcomercioseguridad.services.AuthService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "API para gestión de autenticación")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Operation(summary = "Iniciar sesión",
            description = "Autentica a un usuario y retorna un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Iniciando proceso de login para usuario: {}", loginRequest.getEmail());
        try {
            AuthResponse response = authService.login(loginRequest);
            logger.info("Login exitoso para usuario: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error en login para usuario {}: {}", loginRequest.getEmail(), e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Registrar cliente",
            description = "Registra un nuevo cliente en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
            @ApiResponse(responseCode = "409", description = "El nombre de usuario ya existe")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Iniciando registro de nuevo cliente: {}", registerRequest.getName());
        try {
            AuthResponse response = authService.registerClient(registerRequest);
            logger.info("Cliente registrado exitosamente: {}", registerRequest.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al registrar cliente {}: {}", registerRequest.getName(), e.getMessage(), e);
            throw e;
        }
    }

    @Operation(summary = "Registrar productor",
            description = "Registra un nuevo productor (vendedor local) en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productor registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
            @ApiResponse(responseCode = "409", description = "El nombre de usuario o información del productor ya existe")
    })
    @PostMapping("/register-producer")
    public ResponseEntity<AuthResponse> registerProducer(@Valid @RequestBody ProducerRegisterRequest registerRequest) {
        logger.info("Iniciando registro de nuevo productor: {}", registerRequest.getName());
        try {
            AuthResponse response = authService.registerProducer(registerRequest);
            logger.info("Productor registrado exitosamente: {}, nombre negocio: {}",
                    registerRequest.getName(), registerRequest.getBusinessName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al registrar productor {}: {}", registerRequest.getName(), e.getMessage(), e);
            throw e;
        }
    }
}