package Estenio_Jose.Hotel_Quelimane.Servicos;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Estenio_Jose.Hotel_Quelimane.DTO.DashboardDTO;
import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ClienteRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.PagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.QuartosRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReservaRepositorio;
@Service
public class DashboardServico {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private QuartosRepositorio quartosRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    public DashboardDTO obterResumo() {

        DashboardDTO dto = new DashboardDTO();

        dto.setTotalClientes(clienteRepositorio.count());
        dto.setTotalQuartos(quartosRepositorio.count());
        dto.setReservasAtivas(reservaRepositorio.countByStatus("ATIVA"));

        dto.setReservasFinalizadas(reservaRepositorio.countByStatus("FINALIZADA"));

        return dto;
    }
}