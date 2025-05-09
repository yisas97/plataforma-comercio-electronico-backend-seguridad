package pe.com.prueba.plataformacontrolcomercioseguridad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Client;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.User;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>
{
    Optional<Client> findByUser(User user);
    Optional<Client> findByUserId(Long userId);
}

