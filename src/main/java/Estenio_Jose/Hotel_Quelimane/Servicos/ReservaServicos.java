package Estenio_Jose.Hotel_Quelimane.Servicos;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Estenio_Jose.Hotel_Quelimane.DTO.ReservaDTO;
import Estenio_Jose.Hotel_Quelimane.Entidades.Cliente;
import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.Quartos;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ClienteRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.QuartosRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReservaRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.PagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReciboPagamentoRepositorio;
@Service
public class ReservaServicos {
    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private QuartosRepositorio quartoRepositorio;
    @Autowired
    private PagamentoRepositorio pagamentoRepositorio;

    @Autowired
    private ReciboPagamentoRepositorio reciboPagamentoRepositorio;

    // CRIAR RESERVA
    public Reserva criarReserva(ReservaDTO dto) {

        Cliente cliente = clienteRepositorio.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Quartos quarto = quartoRepositorio.findById(dto.getQuartoId())
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        if (!"DISPONIVEL".equalsIgnoreCase(quarto.getEstado())) {
            throw new RuntimeException("Quarto não está disponível");
        }

        long dias = ChronoUnit.DAYS.between(dto.getDataEntrada(), dto.getDataSaida());

        if (dias <= 0) {
            throw new RuntimeException("Datas inválidas");
        }

        double total = dias * quarto.getPrecoNoite();

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setQuarto(quarto);
        reserva.setDataEntrada(dto.getDataEntrada());
        reserva.setDataSaida(dto.getDataSaida());
        reserva.setValorTotal(total);
        reserva.setStatus("RESERVADA");
        quarto.setEstado("RESERVADO");
        quartoRepositorio.save(quarto);
        return reservaRepositorio.save(reserva);
    }

    // LISTAR
    public List<Reserva> listar() {
        return reservaRepositorio.findAll();
    }

    // BUSCAR
    public Reserva buscar(Long id) {
        return reservaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }

    // CANCELAR RESERVA
    public Reserva cancelar(Long id) {

        Reserva reserva = buscar(id);

        reserva.setStatus("CANCELADA");

        Quartos quarto = reserva.getQuarto();
        quarto.setEstado("DISPONIVEL");

        quartoRepositorio.save(quarto);

        return reservaRepositorio.save(reserva);
    }

    // CHECK-IN
    public Reserva checkin(Long id) {

        Reserva reserva = buscar(id);

        reserva.setStatus("EM_HOSPEDAGEM");

        Quartos quarto = reserva.getQuarto();
        quarto.setEstado("OCUPADO");

        quartoRepositorio.save(quarto);

        return reservaRepositorio.save(reserva);
    }

    // CHECK-OUT
    public Reserva checkout(Long id) {

        Reserva reserva = buscar(id);

        reserva.setStatus("FINALIZADA");

        Quartos quarto = reserva.getQuarto();
        quarto.setEstado("DISPONIVEL");

        quartoRepositorio.save(quarto);

        return reservaRepositorio.save(reserva);
    }

 // ATUALIZAR RESERVA
    public Reserva atualizarReserva(Long id, ReservaDTO dto) {

        Reserva reserva = buscar(id);

        Cliente cliente = clienteRepositorio.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Quartos quarto = quartoRepositorio.findById(dto.getQuartoId())
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        if (!"DISPONIVEL".equalsIgnoreCase(quarto.getEstado())
                && !reserva.getQuarto().getId().equals(quarto.getId())) {
            throw new RuntimeException("Quarto não está disponível");
        }

        long dias = ChronoUnit.DAYS.between(dto.getDataEntrada(), dto.getDataSaida());

        if (dias <= 0) {
            throw new RuntimeException("Datas inválidas");
        }

        double total = dias * quarto.getPrecoNoite();

        // devolver quarto antigo se mudou
        if (!reserva.getQuarto().getId().equals(quarto.getId())) {
            reserva.getQuarto().setEstado("DISPONIVEL");
            quartoRepositorio.save(reserva.getQuarto());

            quarto.setEstado("RESERVADO");
            quartoRepositorio.save(quarto);
        }

        reserva.setCliente(cliente);
        reserva.setQuarto(quarto);
        reserva.setDataEntrada(dto.getDataEntrada());
        reserva.setDataSaida(dto.getDataSaida());
        reserva.setValorTotal(total);

        return reservaRepositorio.save(reserva);
    }
    public void eliminar(Long id) {

        Reserva reserva = reservaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        // =========================
        // 1. PAGAMENTOS
        // =========================
        List<Pagamento> pagamentos = pagamentoRepositorio.findByReserva(reserva);

        for (Pagamento p : pagamentos) {
            reciboPagamentoRepositorio.findByPagamento(p)
                    .ifPresent(reciboPagamentoRepositorio::delete);
        }
        pagamentoRepositorio.deleteAll(pagamentos);

        // =========================
        // 2. LIBERTAR QUARTO
        // =========================
        Quartos quarto = reserva.getQuarto();
        quarto.setEstado("DISPONIVEL");
        quartoRepositorio.save(quarto);

        // =========================
        // 3. ELIMINAR RESERVA
        // =========================
        reservaRepositorio.delete(reserva);
    }
}