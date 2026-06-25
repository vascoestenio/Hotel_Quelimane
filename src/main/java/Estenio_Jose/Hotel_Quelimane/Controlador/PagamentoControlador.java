package Estenio_Jose.Hotel_Quelimane.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.ReciboPagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
import Estenio_Jose.Hotel_Quelimane.Repositorio.PagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReservaRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReciboPagamentoRepositorio;

import java.util.List;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoControlador {

    @Autowired
    private PagamentoRepositorio pagamentoRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private ReciboPagamentoRepositorio reciboRepositorio;

    // ================= LISTAR =================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pagamentos", pagamentoRepositorio.findAll());
        return "pagamentos/lista";
    }

    // ================= NOVO =================
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("pagamento", new Pagamento());
        model.addAttribute("reservas", reservaRepositorio.findAll());
        return "pagamentos/formulario";
    }

    // ================= SALVAR =================
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Pagamento pagamento) {

        Reserva reserva = reservaRepositorio.findById(
                pagamento.getReserva().getId()
        ).orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        pagamento.setReserva(reserva);
        pagamento.setValor(reserva.getValorTotal());

        Pagamento pagoSalvo = pagamentoRepositorio.save(pagamento);

        // ================= RECIBO =================
        ReciboPagamento recibo = reciboRepositorio.findByPagamento(pagoSalvo)
                .orElse(null);

        if (recibo == null) {
            recibo = new ReciboPagamento();
            recibo.setPagamento(pagoSalvo);
            recibo.setReserva(reserva);

            recibo.setNumeroRecibo("REC-" + pagoSalvo.getId());
            recibo.setDataEmissao(java.time.LocalDate.now());

            recibo.setValorTotal(reserva.getValorTotal());
            recibo.setValorPago(pagoSalvo.getValor());
            recibo.setMetodoPagamento(pagoSalvo.getMetodo());
            recibo.setStatus("PAGO");

            if (pagoSalvo.getDataPagamento() != null) {
                recibo.setDataPagamento(pagoSalvo.getDataPagamento().atStartOfDay());
            }

            reciboRepositorio.save(recibo);
        }

        ReciboPagamento reciboFinal = reciboRepositorio.findByPagamento(pagoSalvo)
                .orElseThrow(() -> new RuntimeException("Recibo não foi gerado"));

        // 🔥 CORRETO: usar ID do reciboFinal
        return "redirect:/pagamentos";
    }

    // ================= TESTE =================
    @GetMapping("/teste")
    @ResponseBody
    public String teste() {
        return "OK PAGAMENTO CONTROLLER FUNCIONA";
    }

    // ================= EDITAR =================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Pagamento pagamento = pagamentoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        model.addAttribute("pagamento", pagamento);
        model.addAttribute("reservas", reservaRepositorio.findAll());

        return "pagamentos/formulario";
    }

    // ================= ELIMINAR =================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        pagamentoRepositorio.deleteById(id);
        return "redirect:/pagamentos";
    }

    // ================= VER RECIBO =================
    @GetMapping("/recibo/{id}")
    public String abrirReciboPorPagamento(@PathVariable Long id, Model model) {

        Pagamento pagamento = pagamentoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        ReciboPagamento recibo = reciboRepositorio.findByPagamento(pagamento)
                .orElseThrow(() -> new RuntimeException("Recibo não encontrado"));

        model.addAttribute("recibo", recibo);

        return "recibos/recibo";
    }
}