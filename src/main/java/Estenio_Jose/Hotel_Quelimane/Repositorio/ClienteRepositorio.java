package Estenio_Jose.Hotel_Quelimane.Repositorio;
import org.springframework.data.jpa.repository.JpaRepository;
import Estenio_Jose.Hotel_Quelimane.Entidades.Cliente;
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
	long count();
}
