package Estenio_Jose.Hotel_Quelimane.Controlador;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import Estenio_Jose.Hotel_Quelimane.DTO.ReservaDTO;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
import Estenio_Jose.Hotel_Quelimane.Servicos.ReservaServicos;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ClienteRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.QuartosRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReservaRepositorio;

import org.springframework.ui.Model;
@Controller
@RequestMapping("/reservas")
public class ReservaWebControlador {
    @Autowired
    private ReservaServicos reservaServicos;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private QuartosRepositorio quartoRepositorio;
    @GetMapping("/valor/{id}")
    @ResponseBody
    public Map<String, Object> getValorReserva(@PathVariable Long id) {

        Reserva reserva = reservaRepositorio.findById(id)
                .orElseThrow();

        return Map.of("valor", reserva.getValorTotal());
    }
  
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("reservas", reservaServicos.listar());
        return "reservas/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute("reserva", new ReservaDTO());

        model.addAttribute("clientes", clienteRepositorio.findAll());
        model.addAttribute("quartos", quartoRepositorio.findAll());

        return "reservas/formulario";
    }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ReservaDTO dto) {
    	reservaServicos.criarReserva(dto);
        return "redirect:/reservas";
    }

    @GetMapping("/checkin/{id}")
    public String checkin(@PathVariable Long id) {
        reservaServicos.checkin(id);
        return "redirect:/reservas";
    }

    @GetMapping("/checkout/{id}")
    public String checkout(@PathVariable Long id) {
        reservaServicos.checkout(id);
        return "redirect:/reservas";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        reservaServicos.eliminar(id);
        return "redirect:/reservas";
    }
    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        reservaServicos.cancelar(id);
        return "redirect:/reservas";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Reserva reserva = reservaServicos.buscar(id);

        ReservaDTO dto = new ReservaDTO();
        dto.setClienteId(reserva.getCliente().getId());
        dto.setQuartoId(reserva.getQuarto().getId());
        dto.setDataEntrada(reserva.getDataEntrada());
        dto.setDataSaida(reserva.getDataSaida());

        model.addAttribute("reserva", dto);
        model.addAttribute("id", id);

        model.addAttribute("clientes", clienteRepositorio.findAll());
        model.addAttribute("quartos", quartoRepositorio.findAll());

        return "reservas/formulario";
    }
    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute ReservaDTO dto) {

        reservaServicos.atualizarReserva(id, dto);

        return "redirect:/reservas";
    }
}