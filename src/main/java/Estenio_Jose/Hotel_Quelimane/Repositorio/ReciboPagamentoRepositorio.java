package Estenio_Jose.Hotel_Quelimane.Repositorio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.ReciboPagamento;
@Repository
public interface ReciboPagamentoRepositorio
        extends JpaRepository<ReciboPagamento, Long> {

    /**
     * Procura um recibo através do ID do pagamento associado.
     */
    Optional<ReciboPagamento> findByPagamento(Pagamento pagamento);

}