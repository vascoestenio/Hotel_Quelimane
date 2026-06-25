package Estenio_Jose.Hotel_Quelimane.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Estenio_Jose.Hotel_Quelimane.Entidades.Cliente;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ClienteRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.PagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReciboPagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReservaRepositorio;

@Controller
@RequestMapping("/clientes")
public class ClienteControlador {
	@Autowired
	private PagamentoRepositorio pagamentoRepositorio;

	@Autowired
	private ReciboPagamentoRepositorio reciboPagamentoRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteRepositorio.findAll());
        return "clientes/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Cliente cliente) {
        clienteRepositorio.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        model.addAttribute("cliente", cliente);
        return "clientes/formulario";
    }

    @Autowired
    private ReservaRepositorio reservaRepository;

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        var reservas = reservaRepository.findByCliente(cliente);

        for (Reserva reserva : reservas) {

            // 1. buscar pagamentos da reserva
            var pagamentos = pagamentoRepositorio.findByReserva(reserva);

            for (var pagamento : pagamentos) {

                // 2. apagar recibo ligado ao pagamento
                reciboPagamentoRepositorio.findByPagamento(pagamento)
                        .ifPresent(reciboPagamentoRepositorio::delete);
            }

            // 3. apagar pagamentos
            pagamentoRepositorio.deleteAll(pagamentos);
        }

        // 4. apagar reservas
        reservaRepository.deleteAll(reservas);

        // 5. apagar cliente
        clienteRepositorio.delete(cliente);

        return "redirect:/clientes";
    }
}
