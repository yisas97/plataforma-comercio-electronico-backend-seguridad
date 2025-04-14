package pe.com.prueba.plataformacontrolcomercioseguridad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.Producer;
import pe.com.prueba.plataformacontrolcomercioseguridad.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long>
{
    Optional<Producer> findByUser(User user);
    Optional<Producer> findByUserId(Long userId);
    List<Producer> findByApproved(boolean approved);
}
