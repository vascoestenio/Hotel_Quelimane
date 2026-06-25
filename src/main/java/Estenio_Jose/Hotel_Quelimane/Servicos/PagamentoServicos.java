package Estenio_Jose.Hotel_Quelimane.Servicos;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import Estenio_Jose.Hotel_Quelimane.DTO.PagamentoDTO;
import Estenio_Jose.Hotel_Quelimane.Entidades.Pagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.ReciboPagamento;
import Estenio_Jose.Hotel_Quelimane.Entidades.Reserva;
import Estenio_Jose.Hotel_Quelimane.Repositorio.PagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReciboPagamentoRepositorio;
import Estenio_Jose.Hotel_Quelimane.Repositorio.ReservaRepositorio;
@Service
public class PagamentoServicos {

    @Autowired
    private PagamentoRepositorio pagamentoRepositorio;

    @Autowired
    private ReciboPagamentoRepositorio reciboRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    public Pagamento efectuarPagamento(PagamentoDTO dto) {

        Reserva reserva = reservaRepositorio.findById(dto.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        // =========================
        // 1. PAGAMENTO
        // =========================
        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setValor(dto.getValorPago());
        pagamento.setMetodo(dto.getMetodoPagamento());
        pagamento.setDataPagamento(LocalDate.now());

        pagamento = pagamentoRepositorio.save(pagamento);

        // =========================
        // 2. RECIBO
        // =========================
        ReciboPagamento recibo = new ReciboPagamento();
        recibo.setPagamento(pagamento);
        recibo.setDataEmissao(LocalDate.now());
        recibo.setNumeroRecibo("REC-" + pagamento.getId());

        reciboRepositorio.save(recibo);

        return pagamento;
    }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Pagamento pagamento) {

        // 1. BUSCAR RESERVA COMPLETA
        if (pagamento.getReserva() != null) {

            Reserva reserva = reservaRepositorio.findById(
                    pagamento.getReserva().getId()
            ).orElseThrow();

            pagamento.setValor(reserva.getValorTotal());
        }

        // 2. SALVAR PAGAMENTO
        Pagamento pagoSalvo = pagamentoRepositorio.save(pagamento);

        // 3. CRIAR RECIBO AUTOMATICAMENTE
        ReciboPagamento reciboExistente = reciboRepositorio
                .findByPagamento(pagoSalvo)
                .orElse(null);

        if (reciboExistente == null) {

            ReciboPagamento recibo = new ReciboPagamento();

            recibo.setPagamento(pagoSalvo);
            recibo.setReserva(pagoSalvo.getReserva());

            recibo.setValorPago(pagoSalvo.getValor());
            recibo.setValorTotal(pagoSalvo.getReserva().getValorTotal());

            recibo.setMetodoPagamento(pagoSalvo.getMetodo());
            recibo.setStatus("PAGO");

            recibo.setDataEmissao(java.time.LocalDate.now());
            recibo.setDataPagamento(java.time.LocalDateTime.now());

            recibo.setNumeroRecibo("REC-" + pagoSalvo.getId());

            reciboRepositorio.save(recibo);
        }

        return "redirect:/pagamentos";
    }
    public ReciboPagamento criarRecibo(Pagamento pagamentoSalvo) {

        // evitar duplicação
    	ReciboPagamento existente = reciboRepositorio
    	        .findByPagamento(pagamentoSalvo)
    	        .orElse(null);

        if (existente != null) {
            return existente;
        }

        ReciboPagamento recibo = new ReciboPagamento();

        // 🔗 RELAÇÕES
        recibo.setPagamento(pagamentoSalvo);
        recibo.setReserva(pagamentoSalvo.getReserva());

        // 💰 VALORES
        if (pagamentoSalvo.getReserva() != null) {
            recibo.setValorTotal(pagamentoSalvo.getReserva().getValorTotal());
        }

        recibo.setValorPago(pagamentoSalvo.getValor());
        recibo.setMetodoPagamento(pagamentoSalvo.getMetodo());

        // 📅 DATAS
        recibo.setDataEmissao(java.time.LocalDate.now());
        recibo.setDataPagamento(java.time.LocalDateTime.now());

        // 📌 STATUS
        recibo.setStatus("PAGO");

        // 🧾 NÚMERO
        recibo.setNumeroRecibo("REC-" + pagamentoSalvo.getId());

        return reciboRepositorio.save(recibo);
    }
}