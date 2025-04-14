package pe.com.prueba.plataformacontrolcomercioseguridad.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.com.prueba.plataformacontrolcomercioseguridad.dto.UserDetailsDTO;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Role;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.User;
import pe.com.prueba.plataformacontrolcomercioseguridad.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService
{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException
    {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }

    public List<UserDetailsDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDetailsDTO)
                .collect(Collectors.toList());
    }

    public List<UserDetailsDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToUserDetailsDTO)
                .collect(Collectors.toList());
    }

    public UserDetailsDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToUserDetailsDTO(user);
    }

    private UserDetailsDTO mapToUserDetailsDTO(User user) {
        return UserDetailsDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .verified(user.isVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }
}