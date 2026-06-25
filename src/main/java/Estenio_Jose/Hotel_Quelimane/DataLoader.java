package Estenio_Jose.Hotel_Quelimane;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import Estenio_Jose.Hotel_Quelimane.Entidades.Perfil;
import Estenio_Jose.Hotel_Quelimane.Entidades.Usuarios;
import Estenio_Jose.Hotel_Quelimane.Repositorio.UsuariosRepositorio;

@Component
public class DataLoader {
	@Bean
	public CommandLineRunner init(UsuariosRepositorio repo, PasswordEncoder encoder) {
	    return args -> {

	        if (repo.findByUsername("admin") == null) {
	            Usuarios admin = new Usuarios();
	            admin.setNome("Administrador");
	            admin.setUsername("admin");
	            admin.setSenha(encoder.encode("admin123"));
	            admin.setPerfil(Perfil.ADMIN);

	            repo.save(admin);
	        }

	        if (repo.findByUsername("gerente") == null) {
	            Usuarios g = new Usuarios();
	            g.setNome("Gerente");
	            g.setUsername("gerente");
	            g.setSenha(encoder.encode("1234"));
	            g.setPerfil(Perfil.GERENTE);

	            repo.save(g);
	        }

	        if (repo.findByUsername("recepcao") == null) {
	            Usuarios r = new Usuarios();
	            r.setNome("Recepcionista");
	            r.setUsername("recepcao");
	            r.setSenha(encoder.encode("1234"));
	            r.setPerfil(Perfil.RECEPCIONISTA);

	            repo.save(r);
	        }
	    };
	}
   
}
