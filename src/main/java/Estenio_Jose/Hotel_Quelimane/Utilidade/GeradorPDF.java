package Estenio_Jose.Hotel_Quelimane.Utilidade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import Estenio_Jose.Hotel_Quelimane.Entidades.ReciboPagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;

public class GeradorPDF {

    public static ByteArrayInputStream gerarRecibo(ReciboPagamento recibo) {

        Document documento = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(documento, out);
            documento.open();

            Pagamento pagamento = recibo.getPagamento();

            documento.add(new Paragraph("======================================"));
            documento.add(new Paragraph("           HOTEL QUELIMANE            "));
            documento.add(new Paragraph("======================================"));
            documento.add(new Paragraph("RECIBO DE PAGAMENTO"));
            documento.add(new Paragraph(" "));

            documento.add(new Paragraph("Recibo Nº: " + recibo.getNumeroRecibo()));
            documento.add(new Paragraph("Pagamento ID: " + pagamento.getId()));

            documento.add(new Paragraph(
                    "Cliente: " + pagamento.getReserva().getCliente().getNome()));

            documento.add(new Paragraph(
                    "Quarto Nº: " + pagamento.getReserva().getQuarto().getNumero()));

            documento.add(new Paragraph(
                    "Data Entrada: " + pagamento.getReserva().getDataEntrada()));

            documento.add(new Paragraph(
                    "Data Saída: " + pagamento.getReserva().getDataSaida()));

            documento.add(new Paragraph(" "));

            documento.add(new Paragraph(
                    "Valor: " + pagamento.getValor() + " MZN"));

            documento.add(new Paragraph(
                    "Método de Pagamento: " + pagamento.getMetodo()));

            documento.add(new Paragraph(
                    "Data do Pagamento: " + pagamento.getDataPagamento()));

            documento.add(new Paragraph(" "));

            documento.add(new Paragraph("Obrigado pela preferência."));
            documento.add(new Paragraph("Volte sempre ao Hotel Quelimane!"));

            documento.close();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do recibo", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}