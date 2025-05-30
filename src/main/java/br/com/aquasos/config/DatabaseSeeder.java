package br.com.aquasos.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.aquasos.model.PedidoAgua;
import br.com.aquasos.model.PontoDistribuicao;
import br.com.aquasos.model.Usuario;
import br.com.aquasos.model.enums.StatusPedido;
import br.com.aquasos.model.enums.UserRole;
import br.com.aquasos.repository.PedidoAguaRepository;
import br.com.aquasos.repository.PontoDistribuicaoRepository;
import br.com.aquasos.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseSeeder {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PontoDistribuicaoRepository pontoDistribuicaoRepository;

    @Autowired
    private PedidoAguaRepository pedidoAguaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (usuarioRepository.count() == 0) {
            var usuarios = List.of(
                    Usuario.builder()
                            .nome("João Solicitante")
                            .email("joao@aqua.com")
                            .password(passwordEncoder.encode("joao123"))
                            .role(UserRole.ADMIN)
                            .build(),
                    Usuario.builder()
                            .nome("Ana Voluntária")
                            .email("ana@aqua.com")
                            .password(passwordEncoder.encode("ana123"))
                            .role(UserRole.USER)
                            .build());
            usuarioRepository.saveAll(usuarios);
        }

        if (pontoDistribuicaoRepository.count() == 0) {
            var pontos = List.of(
                    PontoDistribuicao.builder()
                            .nome("Ponto Central")
                            .endereco("Rua Principal, 100")
                            .cidade("São Paulo")
                            .capacidadeTotalLitros(10000)
                            .build(),
                    PontoDistribuicao.builder()
                            .nome("Ponto Norte")
                            .endereco("Avenida Norte, 200")
                            .cidade("Guarulhos")
                            .capacidadeTotalLitros(8000)
                            .build());
            pontoDistribuicaoRepository.saveAll(pontos);
        }

        if (pedidoAguaRepository.count() == 0) {
            Usuario usuario = usuarioRepository.findAll().get(0);
            Usuario usuario2 = usuarioRepository.findAll().get(1);
            PontoDistribuicao ponto = pontoDistribuicaoRepository.findAll().get(0);
            PontoDistribuicao ponto2 = pontoDistribuicaoRepository.findAll().get(1);

            var pedidos = List.of(
                    PedidoAgua.builder()
                            .usuario(usuario)
                            .ponto(ponto)
                            .quantidadeLitros(500)
                            .dataSolicitacao(java.time.LocalDate.now())
                            .status(StatusPedido.PENDENTE)
                            .build(),
                    PedidoAgua.builder()
                            .usuario(usuario2)
                            .ponto(ponto2)
                            .quantidadeLitros(1000)
                            .dataSolicitacao(java.time.LocalDate.now())
                            .status(StatusPedido.ENTREGUE)
                            .build());
            pedidoAguaRepository.saveAll(pedidos);
        }
    }
}