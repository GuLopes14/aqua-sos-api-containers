package br.com.aquasos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.aquasos.model.PedidoAgua;
import br.com.aquasos.model.PontoDistribuicao;
import br.com.aquasos.model.Usuario;
import br.com.aquasos.model.dto.PedidoAguaDTO;
import br.com.aquasos.repository.PedidoAguaRepository;
import br.com.aquasos.repository.PontoDistribuicaoRepository;
import br.com.aquasos.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos-agua")
public class PedidoAguaController {

        public record PedidoAguaFilter(
                        Long usuarioId,
                        Long pontoDistribuicaoId,
                        Integer quantidadeLitros,
                        String status) {
        }

        private final Logger log = LoggerFactory.getLogger(getClass());

        @Autowired
        private PedidoAguaRepository repository;

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private PontoDistribuicaoRepository pontoDistribuicaoRepository;

        @GetMapping
        @Cacheable("pedidosAgua")
        @Operation(tags = "PedidoAgua", summary = "Listar pedidos de água", description = "Devolve a lista de pedidos de água com filtros e paginação")
        public Page<PedidoAguaDTO> index(PedidoAguaFilter filters, Pageable pageable) {
                log.info("Listando pedidos de água com filtros e paginação");
                var specification = br.com.aquasos.specification.PedidoAguaSpecification.withFilters(filters);
                return repository.findAll(specification, pageable)
                                .map(p -> new PedidoAguaDTO(
                                                p.getId(),
                                                p.getUsuario().getId(),
                                                p.getPonto().getId(),
                                                p.getQuantidadeLitros(),
                                                p.getDataSolicitacao(),
                                                p.getStatus()));
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(tags = "PedidoAgua", summary = "Cadastrar pedido de água", description = "Cadastra um novo pedido de água", responses = @ApiResponse(responseCode = "400", description = "Validação falhou"))
        @CacheEvict(value = "pedidosAgua", allEntries = true)
        public PedidoAguaDTO create(@RequestBody @Valid PedidoAguaDTO dto) {
                log.info("Cadastrando pedido de água para usuário {}", dto.usuarioId());
                Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Usuário não encontrado"));
                PontoDistribuicao ponto = pontoDistribuicaoRepository.findById(dto.pontoDistribuicaoId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Ponto de distribuição não encontrado"));

                PedidoAgua pedido = new PedidoAgua();
                pedido.setUsuario(usuario);
                pedido.setPonto(ponto);
                pedido.setQuantidadeLitros(dto.quantidadeLitros());
                pedido.setDataSolicitacao(dto.dataSolicitacao());
                pedido.setStatus(dto.status());

                PedidoAgua saved = repository.save(pedido);

                return new PedidoAguaDTO(
                                saved.getId(),
                                saved.getUsuario().getId(),
                                saved.getPonto().getId(),
                                saved.getQuantidadeLitros(),
                                saved.getDataSolicitacao(),
                                saved.getStatus());
        }

        @GetMapping("{id}")
        @Operation(tags = "PedidoAgua", summary = "Buscar pedido de água por ID", description = "Busca um pedido de água pelo seu ID")
        public PedidoAguaDTO get(@PathVariable Long id) {
                log.info("Buscando pedido de água {}", id);
                PedidoAgua pedido = getPedido(id);
                return new PedidoAguaDTO(
                                pedido.getId(),
                                pedido.getUsuario().getId(),
                                pedido.getPonto().getId(),
                                pedido.getQuantidadeLitros(),
                                pedido.getDataSolicitacao(),
                                pedido.getStatus());
        }

        @DeleteMapping("{id}")
        @CacheEvict(value = "pedidosAgua", allEntries = true)
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @Operation(tags = "PedidoAgua", summary = "Deletar pedido de água por ID", description = "Remove um pedido de água pelo seu ID")
        public void destroy(@PathVariable Long id) {
                log.info("Apagando pedido de água {}", id);
                PedidoAgua pedido = getPedido(id);
                repository.delete(pedido);
                log.info("Pedido de água {} apagado com sucesso", id);
        }

        @PutMapping("{id}")
        @CacheEvict(value = "pedidosAgua", allEntries = true)
        @Operation(tags = "PedidoAgua", summary = "Atualizar pedido de água por ID", description = "Atualiza um pedido de água pelo seu ID")
        public PedidoAguaDTO update(@PathVariable Long id, @RequestBody @Valid PedidoAguaDTO dto) {
                log.info("Atualizando pedido de água {} para usuário {}", id, dto.usuarioId());
                PedidoAgua pedido = getPedido(id);

                Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Usuário não encontrado"));
                PontoDistribuicao ponto = pontoDistribuicaoRepository.findById(dto.pontoDistribuicaoId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Ponto de distribuição não encontrado"));

                pedido.setUsuario(usuario);
                pedido.setPonto(ponto);
                pedido.setQuantidadeLitros(dto.quantidadeLitros());
                pedido.setDataSolicitacao(dto.dataSolicitacao());
                pedido.setStatus(dto.status());

                PedidoAgua updated = repository.save(pedido);

                return new PedidoAguaDTO(
                                updated.getId(),
                                updated.getUsuario().getId(),
                                updated.getPonto().getId(),
                                updated.getQuantidadeLitros(),
                                updated.getDataSolicitacao(),
                                updated.getStatus());
        }

        private PedidoAgua getPedido(Long id) {
                return repository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Pedido de água não encontrado"));
        }
}