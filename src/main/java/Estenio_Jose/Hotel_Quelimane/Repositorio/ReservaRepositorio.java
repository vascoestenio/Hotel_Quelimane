package Estenio_Jose.Hotel_Quelimane.Repositorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import Estenio_Jose.Hotel_Quelimane.Entidades.Cliente;
import Estenio_Jose.Hotel_Quelimane.Entidades.Quartos;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
public interface ReservaRepositorio extends JpaRepository<Reserva, Long> {
	List<Reserva> findByCliente(Cliente cliente);
	List<Reserva> findByQuarto(Quartos quarto);
	@Query("""
 	       SELECT COUNT(r)
 	       FROM Reserva r
 	       """)
 	Long totalReservas();

 	@Query("""
 	       SELECT COALESCE(SUM(r.valorTotal),0)
 	       FROM Reserva r
 	       """)
 	Double valorTotalReservas();

    long countByStatus(String status);
    
    long count();
    
    

}
