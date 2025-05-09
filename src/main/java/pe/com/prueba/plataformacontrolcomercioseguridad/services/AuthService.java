package pe.com.prueba.plataformacontrolcomercioseguridad.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.AuthResponse;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.LoginRequest;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.ProducerRegisterRequest;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.RegisterRequest;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Client;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Producer;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Role;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.User;
import pe.com.prueba.plataformacontrolcomercioseguridad.repository.ClientRepository;
import pe.com.prueba.plataformacontrolcomercioseguridad.repository.ProducerRepository;
import pe.com.prueba.plataformacontrolcomercioseguridad.repository.UserRepository;
import pe.com.prueba.plataformacontrolcomercioseguridad.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProducerRepository producerRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        AuthResponse.AuthResponseBuilder responseBuilder = AuthResponse.builder()
                .token(jwt)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .verified(user.isVerified());

        if (user.getRole() == Role.ROLE_PRODUCER) {
            Producer producer = producerRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Perfil de productor no encontrado"));
            responseBuilder.approved(producer.isApproved());
        }

        return responseBuilder.build();
    }

    @Transactional
    public AuthResponse registerClient(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email ya está en uso");
        }

        // Crear usuario
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ROLE_CLIENT)
                .verified(true) // Los clientes no necesitan verificación
                .build();

        user = userRepository.save(user);

        // Crear perfil de cliente
        Client client = Client.builder()
                .user(user)
                .build();

        clientRepository.save(client);

        // Generar token JWT
        String jwt = tokenProvider.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(jwt)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .verified(user.isVerified())
                .build();
    }

    @Transactional
    public AuthResponse registerProducer(ProducerRegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email ya está en uso");
        }

        // Crear usuario
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ROLE_PRODUCER)
                .verified(false) // Los productores requieren verificación
                .build();

        user = userRepository.save(user);

        // Crear perfil de productor
        Producer producer = Producer.builder()
                .user(user)
                .businessName(registerRequest.getBusinessName())
                .description(registerRequest.getDescription())
                .location(registerRequest.getLocation())
                .phone(registerRequest.getPhone())
                .approved(false)
                .build();

        producerRepository.save(producer);

        return AuthResponse.builder()
                .token(null) // No token hasta que sea aprobado
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .verified(user.isVerified())
                .approved(false)
                .build();
    }
}