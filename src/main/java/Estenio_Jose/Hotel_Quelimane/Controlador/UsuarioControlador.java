package Estenio_Jose.Hotel_Quelimane.Controlador;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import Estenio_Jose.Hotel_Quelimane.Entidades.Usuarios;
import Estenio_Jose.Hotel_Quelimane.Servicos.UsuariosServicos;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private final UsuariosServicos usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioControlador(UsuariosServicos usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    // NOVO
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuarios());
        return "usuarios/form";
    }

    // SALVAR
    @PostMapping("/salvar")
    public String salvar(Usuarios usuario) {

        // se tiver password, encriptar
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuarioService.salvar(usuario);
        return "redirect:/usuarios";
    }

    // EDITAR (abrir formulário com dados)
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Usuarios usuario = usuarioService.buscarPorId(id);

        model.addAttribute("usuario", usuario);

        return "usuarios/form";
    }

    // ATUALIZAR (opcional, pode usar salvar também)
    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, Usuarios usuario) {

        usuario.setId(id);

        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuarioService.salvar(usuario);

        return "redirect:/usuarios";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        usuarioService.eliminar(id);

        return "redirect:/usuarios";
    }
}