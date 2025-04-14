package pe.com.prueba.plataformacontrolcomercioseguridad.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerClient(registerRequest));
    }

    @PostMapping("/register-producer")
    public ResponseEntity<AuthResponse> registerProducer(@Valid @RequestBody ProducerRegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerProducer(registerRequest));
    }
}