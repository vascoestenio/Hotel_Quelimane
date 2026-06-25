package Estenio_Jose.Hotel_Quelimane.Repositorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.ReciboPagamento;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
public interface PagamentoRepositorio extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByReserva(Reserva reserva);
 // ==========================
    // RECEITAS GERAIS
    // ==========================

    @Query("""
           SELECT COALESCE(SUM(p.valor),0)
           FROM Pagamento p
           """)
    Double receitaTotal();

    @Query("""
           SELECT COUNT(p)
           FROM Pagamento p
           """)
    Long totalPagamentos();

    @Query("""
           SELECT COALESCE(SUM(p.valor),0)
           FROM Pagamento p
           WHERE p.dataPagamento = CURRENT_DATE
           """)
    Double receitaHoje();

    @Query("""
           SELECT COALESCE(SUM(p.valor),0)
           FROM Pagamento p
           WHERE p.dataPagamento BETWEEN :inicio AND :fim
           """)
    Double receitaPorPeriodo(LocalDate inicio,
                             LocalDate fim);

    // ==========================
    // RECEITA POR MÉTODO
    // ==========================

    @Query("""
           SELECT p.metodo,
                  COALESCE(SUM(p.valor),0)
           FROM Pagamento p
           GROUP BY p.metodo
           ORDER BY SUM(p.valor) DESC
           """)
    List<Object[]> receitaPorMetodo();

    // ==========================
    // CLIENTES QUE MAIS GASTARAM
    // ==========================

    @Query("""
           SELECT p.reserva.cliente.nome,
                  COALESCE(SUM(p.valor),0)
           FROM Pagamento p
           GROUP BY p.reserva.cliente.nome
           ORDER BY SUM(p.valor) DESC
           """)
    List<Object[]> clientesMaisGastaram();

    // ==========================
    // QUARTOS MAIS RENTÁVEIS
    // ==========================

    @Query("""
           SELECT p.reserva.quarto.numero,
                  COALESCE(SUM(p.valor),0)
           FROM Pagamento p
           GROUP BY p.reserva.quarto.numero
           ORDER BY SUM(p.valor) DESC
           """)
    List<Object[]> quartosMaisRentaveis();

    // ==========================
    // RECEITA POR TIPO DE QUARTO
    // ==========================

    @Query("""
           SELECT p.reserva.quarto.tipo,
                  COALESCE(SUM(p.valor),0)
           FROM Pagamento p
           GROUP BY p.reserva.quarto.tipo
           ORDER BY SUM(p.valor) DESC
           """)
    List<Object[]> receitaPorTipoQuarto();

    // ==========================
    // ÚLTIMOS PAGAMENTOS
    // ==========================

    List<Pagamento> findTop10ByOrderByDataPagamentoDesc();
    }