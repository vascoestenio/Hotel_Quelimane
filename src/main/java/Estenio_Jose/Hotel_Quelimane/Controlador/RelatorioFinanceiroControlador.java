package Estenio_Jose.Hotel_Quelimane.Controlador;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Estenio_Jose.Hotel_Quelimane.Repositorio.ClienteRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.PagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.QuartosRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReservaRepositorio;

@Controller
@RequestMapping("/relatorios")
public class RelatorioFinanceiroControlador {

    @Autowired
    private PagamentoRepositorio pagamentoRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private QuartosRepositorio quartosRepositorio;
    
    @GetMapping("/financeiro")
    public String financeiro(Model model) {

        model.addAttribute("receitaTotal",
                pagamentoRepositorio.receitaTotal());

        model.addAttribute("receitaHoje",
                pagamentoRepositorio.receitaHoje());

        model.addAttribute("totalPagamentos",
                pagamentoRepositorio.totalPagamentos());

        model.addAttribute("metodos",
                pagamentoRepositorio.receitaPorMetodo());

        model.addAttribute("clientes",
                pagamentoRepositorio.clientesMaisGastaram());

        model.addAttribute("quartos",
                pagamentoRepositorio.quartosMaisRentaveis());
        model.addAttribute("totalClientes",
                clienteRepositorio.count());

        model.addAttribute("totalReservas",
                reservaRepositorio.count());

        model.addAttribute("totalQuartos",
                quartosRepositorio.count());

        return "relatorios/financeiro";
    
    }
}