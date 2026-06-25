package Estenio_Jose.Hotel_Quelimane.Controlador;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.ReciboPagamento;
import Estenio_Jose.Hotel_Quelimane.Repositorio.PagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReciboPagamentoRepositorio;
@Controller
@RequestMapping("/recibos")
public class ReciboPagamentoControlador {

    @Autowired
    private ReciboPagamentoRepositorio reciboRepositorio;
    @Autowired
    private PagamentoRepositorio pagamentoRepositorio;
    // =========================
    // LISTAR RECIBOS
    // =========================
    @GetMapping
    public String listar(Model model) {

        List<ReciboPagamento> recibos = reciboRepositorio.findAll();

        model.addAttribute("recibos", recibos);

        return "recibos/lista";
    }

    // =========================
    // NOVO RECIBO
    // =========================
    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute("recibo", new ReciboPagamento());

        return "recibos/formulario";
    }

    // =========================
    // SALVAR RECIBO
    // =========================
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ReciboPagamento recibo) {

        reciboRepositorio.save(recibo);

        return "redirect:/recibos";
    }

    // =========================
    // VER RECIBO
    // =========================

    @GetMapping("/ver/{id}")
    public String verRecibo(@PathVariable Long id, Model model) {

        Pagamento pagamento = pagamentoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Pagamento não encontrado: " + id));

        ReciboPagamento recibo = reciboRepositorio.findByPagamento(pagamento)
                .orElseThrow(() -> new RuntimeException(
                        "Recibo não encontrado para pagamento ID: " + id));

        model.addAttribute("recibo", recibo);

        return "recibos/recibo";
    }
    // =========================
    // EDITAR RECIBO
    // =========================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Pagamento pagamento = pagamentoRepositorio.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Pagamento não encontrado: " + id));

        ReciboPagamento recibo = reciboRepositorio.findByPagamento(pagamento)
                .orElseThrow(() ->
                        new RuntimeException("Recibo não encontrado para o pagamento: " + id));

        model.addAttribute("recibo", recibo);

        return "recibos/formulario";
    }

    // =========================
    // ELIMINAR RECIBO
    // =========================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        reciboRepositorio.deleteById(id);

        return "redirect:/recibos";
    }

    // =========================
    // GERAR PDF
    // =========================
    @GetMapping("/pdf/{id}")
    public void gerarPdf(@PathVariable Long id,
                         HttpServletResponse response)
            throws Exception {
    	System.out.println("ID recebido na URL: " + id);
        ReciboPagamento recibo = reciboRepositorio.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Recibo não encontrado"));

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=recibo-" + id + ".pdf"
        );

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                response.getOutputStream()
        );

        document.open();

        document.add(new Paragraph("===================================="));
        document.add(new Paragraph("        RECIBO DE PAGAMENTO DO HOTEL - QUELIMANE"));
        document.add(new Paragraph("===================================="));
        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "ID Recibo: " + recibo.getId()));

        document.add(new Paragraph(
                "Número: " + recibo.getNumeroRecibo()));

        document.add(new Paragraph(
                "Data Emissão: " + recibo.getDataEmissao()));

        document.add(new Paragraph(" "));

        if (recibo.getReserva() != null) {

            document.add(new Paragraph(
                    "Reserva ID: "
                            + recibo.getReserva().getId()));

            document.add(new Paragraph(
                    "Cliente: "
                            + recibo.getReserva()
                            .getCliente()
                            .getNome()));

            document.add(new Paragraph(
                    "Quarto: "
                            + recibo.getReserva()
                            .getQuarto()
                            .getNumero()));
        }

        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "Valor Total: "
                        + recibo.getValorTotal() + " MT"));

        document.add(new Paragraph(
                "Valor Pago: "
                        + recibo.getValorPago() + " MT"));

        document.add(new Paragraph(
                "Método: "
                        + recibo.getMetodoPagamento()));

        document.add(new Paragraph(
                "Status: "
                        + recibo.getStatus()));

        document.add(new Paragraph(
                "Data Pagamento: "
                        + recibo.getDataPagamento()));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("===================================="));
        document.add(new Paragraph("         HOTEL ENGENHEIROS ESTENIO . JOSE . ORISVALDO"));
        document.add(new Paragraph("===================================="));

        document.close();
    }
}