package Estenio_Jose.Hotel_Quelimane.Entidades;
import jakarta.persistence.*;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "quartos")
public class Quartos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "preco_noite", nullable = false)
    private Double precoNoite;

    @Column(nullable = false)
    private String estado; // DISPONIVEL, RESERVADO, etc.

    // Construtor vazio (OBRIGATÓRIO)
    public Quartos() {
    }

    // Construtor opcional
    public Quartos(Integer numero, String tipo, Double precoNoite, String estado) {
        this.numero = numero;
        this.tipo = tipo;
        this.precoNoite = precoNoite;
        this.estado = estado;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPrecoNoite() {
        return precoNoite;
    }

    public void setPrecoNoite(Double precoNoite) {
        this.precoNoite = precoNoite;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Quartos{" +
                "id=" + id +
                ", numero=" + numero +
                ", tipo='" + tipo + '\'' +
                ", precoNoite=" + precoNoite +
                ", estado='" + estado + '\'' +
                '}';
    }
    @OneToMany(mappedBy = "quarto",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
 private List<Reserva> reservas;
}