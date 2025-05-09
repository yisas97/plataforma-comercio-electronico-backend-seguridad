package pe.com.prueba.plataformacontrolcomercioseguridad.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.UserDetailsDTO;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Role;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.User;
import pe.com.prueba.plataformacontrolcomercioseguridad.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDetailsDTO> getCurrentUser() {
        logger.info("Obteniendo datos del usuario actual");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        logger.debug("Usuario autenticado: ID={}, username={}", user.getId(), user.getUsername());
        UserDetailsDTO userDetails = userService.getUserById(user.getId());
        logger.info("Datos del usuario actual obtenidos exitosamente");
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        logger.info("Solicitud para obtener todos los usuarios");
        List<UserDetailsDTO> users = userService.getAllUsers();
        logger.info("Se encontraron {} usuarios en total", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsDTO>> getUsersByRole(@PathVariable String role) {
        logger.info("Buscando usuarios con rol: {}", role);
        Role userRole = Role.valueOf("ROLE_" + role.toUpperCase());
        List<UserDetailsDTO> users = userService.getUsersByRole(userRole);
        logger.info("Se encontraron {} usuarios con rol {}", users.size(), role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserDetailsDTO> getUserById(@PathVariable Long id) {
        logger.info("Solicitando información del usuario con ID: {}", id);
        try {
            UserDetailsDTO user = userService.getUserById(id);
            logger.info("Información del usuario con ID {} obtenida exitosamente", id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error al obtener usuario con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}