package Estenio_Jose.Hotel_Quelimane.DTO;
public class DashboardDTO {
    private long totalClientes;
    private long totalQuartos;
    private long quartosDisponiveis;
    private long quartosOcupados;
    private long reservasAtivas;
    private long reservasFinalizadas;
    private double taxaOcupacao;
    private double receitaHoje;
    private double receitaAno;
    private long totalPagamentos;
    private double receitaTotal;
    private double receitaMes;
    public double getTaxaOcupacao() {
		return taxaOcupacao;
	}

	public void setTaxaOcupacao(double taxaOcupacao) {
		this.taxaOcupacao = taxaOcupacao;
	}

	public double getReceitaHoje() {
		return receitaHoje;
	}

	public void setReceitaHoje(double receitaHoje) {
		this.receitaHoje = receitaHoje;
	}

	public double getReceitaAno() {
		return receitaAno;
	}

	public void setReceitaAno(double receitaAno) {
		this.receitaAno = receitaAno;
	}

	public long getTotalPagamentos() {
		return totalPagamentos;
	}

	public void setTotalPagamentos(long totalPagamentos) {
		this.totalPagamentos = totalPagamentos;
	}

	public long getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(long totalClientes) {
        this.totalClientes = totalClientes;
    }

    public long getTotalQuartos() {
        return totalQuartos;
    }

    public void setTotalQuartos(long totalQuartos) {
        this.totalQuartos = totalQuartos;
    }

    public long getQuartosDisponiveis() {
        return quartosDisponiveis;
    }

    public void setQuartosDisponiveis(long quartosDisponiveis) {
        this.quartosDisponiveis = quartosDisponiveis;
    }

    public long getQuartosOcupados() {
        return quartosOcupados;
    }

    public void setQuartosOcupados(long quartosOcupados) {
        this.quartosOcupados = quartosOcupados;
    }

    public long getReservasAtivas() {
        return reservasAtivas;
    }

    public void setReservasAtivas(long reservasAtivas) {
        this.reservasAtivas = reservasAtivas;
    }

    public long getReservasFinalizadas() {
        return reservasFinalizadas;
    }

    public void setReservasFinalizadas(long reservasFinalizadas) {
        this.reservasFinalizadas = reservasFinalizadas;
    }

    public double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public double getReceitaMes() {
        return receitaMes;
    }

    public void setReceitaMes(double receitaMes) {
        this.receitaMes = receitaMes;
    }
}
