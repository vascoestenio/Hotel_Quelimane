package Estenio_Jose.Hotel_Quelimane.Servicos;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import Estenio_Jose.Hotel_Quelimane.Entidades.ReciboPagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReciboPagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReservaRepositorio;

@Service
public class ReciboPagamentoServicos {

    @Autowired
    private ReciboPagamentoRepositorio reciboRepo;

    // =========================
    // 1. BUSCAR RECIBO
    // =========================
    public ReciboPagamento buscarPorId(Long id) {
        return reciboRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Recibo não encontrado"));
    }

    // =========================
    // 2. GERAR PDF
    // =========================
    public byte[] gerarPdf(Long reciboId) {

        ReciboPagamento r = buscarPorId(reciboId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("================================"));
            document.add(new Paragraph("        RECIBO DE PAGAMENTO       "));
            document.add(new Paragraph("================================"));

            document.add(new Paragraph("Recibo: " + r.getNumeroRecibo()));
            document.add(new Paragraph("Cliente: " + r.getPagamento().getReserva().getCliente().getNome()));
            document.add(new Paragraph("Quarto: " + r.getPagamento().getReserva().getQuarto().getNumero()));
            document.add(new Paragraph("Data Pagamento: " + r.getPagamento().getDataPagamento()));
            document.add(new Paragraph("Método: " + r.getPagamento().getMetodo()));
            document.add(new Paragraph("Valor: " + r.getPagamento().getValor()));

            document.add(new Paragraph("================================"));

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do recibo", e);
        }

        return out.toByteArray();
    }
}