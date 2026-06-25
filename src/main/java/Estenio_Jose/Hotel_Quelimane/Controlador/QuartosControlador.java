package Estenio_Jose.Hotel_Quelimane.Controlador;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.Quartos;
import Estenio_Jose.Hotel_Quelimane.Repositorio.*;
import org.springframework.ui.Model;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
import java.util.List;
@Controller
@RequestMapping("/quartos")
public class QuartosControlador {

    @Autowired
    private QuartosRepositorio quartosRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private PagamentoRepositorio pagamentoRepositorio;

    @Autowired
    private ReciboPagamentoRepositorio reciboPagamentoRepositorio;
    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute("quarto", new Quartos());

        return "quartos/formulario";
    }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Quartos quarto) {

        quartosRepositorio.save(quarto);

        return "redirect:/quartos";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Quartos quarto = quartosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        model.addAttribute("quarto", quarto);

        return "quartos/formulario";
    }

    @GetMapping
    public String listar(org.springframework.ui.Model model) {
        model.addAttribute("quartos", quartosRepositorio.findAll());
        return "quartos/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        Quartos quarto = quartosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        List<Reserva> reservas = reservaRepositorio.findByQuarto(quarto);

        for (Reserva r : reservas) {

            // 1. buscar pagamentos da reserva
            List<Pagamento> pagamentos = pagamentoRepositorio.findByReserva(r);

            for (Pagamento p : pagamentos) {

                // 2. apagar recibo ligado ao pagamento
                reciboPagamentoRepositorio.findByPagamento(p)
                        .ifPresent(reciboPagamentoRepositorio::delete);
            }

            // 3. apagar pagamentos
            pagamentoRepositorio.deleteAll(pagamentos);
        }

        // 4. apagar reservas
        reservaRepositorio.deleteAll(reservas);

        // 5. apagar quarto
        quartosRepositorio.delete(quarto);

        return "redirect:/quartos";
    }
}