package Estenio_Jose.Hotel_Quelimane.Servicos;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Estenio_Jose.Hotel_Quelimane.Entidades.Usuarios;
import Estenio_Jose.Hotel_Quelimane.Repositorio.UsuariosRepositorio;

@Service
public class UsuariosServicos implements UserDetailsService {

    private final UsuariosRepositorio repositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuariosServicos(UsuariosRepositorio repositorio,
                            PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔐 LOGIN SPRING SECURITY
    @Override
    public UserDetails loadUserByUsername(String username) {

        Usuarios usuario = repositorio.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getSenha(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()))
        );
    }

    // 💾 SALVAR (CRIAR OU ATUALIZAR)
    public Usuarios salvar(Usuarios usuario) {

        // evita duplicar encriptação
        if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$")) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return repositorio.save(usuario);
    }

    // 📋 LISTAR TODOS
    public List<Usuarios> listarTodos() {
        return repositorio.findAll();
    }

    // 🔍 BUSCAR POR ID
    public Usuarios buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado com ID: " + id));
    }

    // ❌ ELIMINAR
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("Utilizador não encontrado para eliminar");
        }
        repositorio.deleteById(id);
    }

    // 🔎 BUSCAR POR USERNAME (opcional mas útil)
    public Usuarios buscarPorUsername(String username) {
        return repositorio.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
    }
}