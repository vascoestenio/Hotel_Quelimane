package Estenio_Jose.Hotel_Quelimane.Controlador;
import Estenio_Jose.Hotel_Quelimane.Repositorio.PagamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class DashboardControlador {
    @Autowired
    private PagamentoRepositorio pagamentoRepositorio;
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("recibos", pagamentoRepositorio.findAll());

        return "dashboard";
    }
}