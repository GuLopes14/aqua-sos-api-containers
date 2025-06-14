package br.com.aquasos.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.aquasos.model.PedidoAgua;
import br.com.aquasos.model.Usuario;
import br.com.aquasos.model.enums.UserRole;
import br.com.aquasos.repository.PedidoAguaRepository;
import br.com.aquasos.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseSeeder {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoAguaRepository pedidoAguaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (usuarioRepository.count() == 0) {
            var usuarios = List.of(
                    Usuario.builder()
                            .nome("Gabriel Jablonski")
                            .email("jaba@aqua.com")
                            .password(passwordEncoder.encode("jaba123"))
                            .role(UserRole.USER)
                            .build(),
                    Usuario.builder()
                            .nome("Renato David")
                            .email("re@aqua.com")
                            .password(passwordEncoder.encode("re123"))
                            .role(UserRole.USER)
                            .build(),
                    Usuario.builder()
                            .nome("Gustavo Lopes")
                            .email("gus@aqua.com")
                            .password(passwordEncoder.encode("gus123"))
                            .role(UserRole.ADMIN)
                            .build());
            usuarioRepository.saveAll(usuarios);
        }

        if (pedidoAguaRepository.count() == 0) {
            Usuario usuario = usuarioRepository.findAll().get(0);
            Usuario usuario2 = usuarioRepository.findAll().get(1);

            var pedidos = List.of(
                    PedidoAgua.builder()
                            .usuario(usuario)
                            .quantidadeLitros(500)
                            .nivelUrgencia(br.com.aquasos.model.enums.NivelUrgencia.MEDIA)
                            .endereco("Rua Principal, 100")
                            .build(),
                    PedidoAgua.builder()
                            .usuario(usuario2)
                            .quantidadeLitros(1000)
                            .nivelUrgencia(br.com.aquasos.model.enums.NivelUrgencia.ALTA)
                            .endereco("Avenida Norte, 200")
                            .build());
            pedidoAguaRepository.saveAll(pedidos);
        }
    }
}