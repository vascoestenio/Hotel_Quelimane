package Estenio_Jose.Hotel_Quelimane.Repositorio;
import org.springframework.data.jpa.repository.JpaRepository;
import Estenio_Jose.Hotel_Quelimane.Entidades.Quartos;
import java.util.Optional;

public interface QuartosRepositorio extends JpaRepository<Quartos, Long> {

    Optional<Quartos> findByNumero(int numero);
    long count();
}