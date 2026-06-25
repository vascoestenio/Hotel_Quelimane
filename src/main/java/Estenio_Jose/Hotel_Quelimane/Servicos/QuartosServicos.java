package Estenio_Jose.Hotel_Quelimane.Servicos;

import Estenio_Jose.Hotel_Quelimane.Entidades.Quartos;
import Estenio_Jose.Hotel_Quelimane.Repositorio.QuartosRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuartosServicos {

    @Autowired
    private QuartosRepositorio quartosRepositorio;

    // LISTAR TODOS
    public List<Quartos> listarTodos() {
        return quartosRepositorio.findAll();
    }

    // BUSCAR POR ID
    public Quartos buscarPorId(Long id) {
        return quartosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
    }

    // SALVAR (CRIAR OU EDITAR)
    public void salvar(Quartos quarto) {

        Optional<Quartos> existente =
                quartosRepositorio.findByNumero(quarto.getNumero());

        if (existente.isPresent()) {

            // se for criação ou outro ID → bloqueia duplicado
            if (quarto.getId() == null ||
                !existente.get().getId().equals(quarto.getId())) {

                throw new RuntimeException("Já existe um quarto com este número!");
            }
        }

        quartosRepositorio.save(quarto);
    }

    // REMOVER
    public void remover(Long id) {
        quartosRepositorio.deleteById(id);
    }
}