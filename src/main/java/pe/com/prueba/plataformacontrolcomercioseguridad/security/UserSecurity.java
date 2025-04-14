package pe.com.prueba.plataformacontrolcomercioseguridad.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.User;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    /**
     * Comprueba si el usuario autenticado es el mismo que el solicitado
     */
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            return false;
        }

        return user.getId().equals(userId);
    }
}