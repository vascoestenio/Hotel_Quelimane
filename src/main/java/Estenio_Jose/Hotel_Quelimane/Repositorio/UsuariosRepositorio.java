package Estenio_Jose.Hotel_Quelimane.Repositorio;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Estenio_Jose.Hotel_Quelimane.Entidades.Usuarios;

public interface UsuariosRepositorio extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByUsername(String username);
}
