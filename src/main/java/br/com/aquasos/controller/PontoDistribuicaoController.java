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

import br.com.aquasos.model.PontoDistribuicao;
import br.com.aquasos.model.dto.PontoDistribuicaoDTO;
import br.com.aquasos.repository.PontoDistribuicaoRepository;
import br.com.aquasos.specification.PontoDistribuicaoSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pontos-distribuicao")
public class PontoDistribuicaoController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public record PontoDistribuicaoFilter(
            String nome,
            String endereco,
            String cidade,
            Integer capacidadeTotalLitros) {
    }

    @Autowired
    private PontoDistribuicaoRepository repository;

    @GetMapping
    @Cacheable("pontosDistribuicao")
    @Operation(tags = "PontoDistribuicao", summary = "Listar pontos de distribuição", description = "Devolve a lista de pontos de distribuição com filtros e paginação")
    public Page<PontoDistribuicaoDTO> index(PontoDistribuicaoFilter filters, Pageable pageable) {
        log.info("Listando pontos de distribuição com filtros e paginação");
        var specification = PontoDistribuicaoSpecification.withFilters(filters);
        return repository.findAll(specification, pageable)
                .map(p -> new PontoDistribuicaoDTO(
                        p.getId(),
                        p.getNome(),
                        p.getEndereco(),
                        p.getCidade(),
                        p.getCapacidadeTotalLitros()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags = "PontoDistribuicao", summary = "Cadastrar ponto de distribuição", description = "Cadastra um novo ponto de distribuição", responses = @ApiResponse(responseCode = "400", description = "Validação falhou"))
    @CacheEvict(value = "pontosDistribuicao", allEntries = true)
    public PontoDistribuicaoDTO create(@RequestBody @Valid PontoDistribuicaoDTO dto) {
        log.info("Cadastrando ponto de distribuição {}", dto.nome());
        PontoDistribuicao ponto = new PontoDistribuicao();
        ponto.setNome(dto.nome());
        ponto.setEndereco(dto.endereco());
        ponto.setCidade(dto.cidade());
        ponto.setCapacidadeTotalLitros(dto.capacidadeTotalLitros());
        PontoDistribuicao saved = repository.save(ponto);
        return new PontoDistribuicaoDTO(
                saved.getId(),
                saved.getNome(),
                saved.getEndereco(),
                saved.getCidade(),
                saved.getCapacidadeTotalLitros());
    }

    @GetMapping("{id}")
    @Operation(tags = "PontoDistribuicao", summary = "Buscar ponto de distribuição por ID", description = "Busca um ponto de distribuição pelo seu ID")
    public PontoDistribuicaoDTO get(@PathVariable Long id) {
        log.info("Buscando ponto de distribuição {}", id);
        PontoDistribuicao ponto = getPonto(id);
        return new PontoDistribuicaoDTO(
                ponto.getId(),
                ponto.getNome(),
                ponto.getEndereco(),
                ponto.getCidade(),
                ponto.getCapacidadeTotalLitros());
    }

    @DeleteMapping("{id}")
    @CacheEvict(value = "pontosDistribuicao", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(tags = "PontoDistribuicao", summary = "Deletar ponto de distribuição por ID", description = "Remove um ponto de distribuição pelo seu ID")
    public void destroy(@PathVariable Long id) {
        log.info("Apagando ponto de distribuição {}", id);
        PontoDistribuicao ponto = getPonto(id);
        repository.delete(ponto);
        log.info("Ponto de distribuição {} apagado com sucesso", id);
    }

    @PutMapping("{id}")
    @CacheEvict(value = "pontosDistribuicao", allEntries = true)
    @Operation(tags = "PontoDistribuicao", summary = "Atualizar ponto de distribuição por ID", description = "Atualiza um ponto de distribuição pelo seu ID")
    public PontoDistribuicaoDTO update(@PathVariable Long id, @RequestBody @Valid PontoDistribuicaoDTO dto) {
        log.info("Atualizando ponto de distribuição {} para {}", id, dto.nome());
        PontoDistribuicao ponto = getPonto(id);
        ponto.setNome(dto.nome());
        ponto.setEndereco(dto.endereco());
        ponto.setCidade(dto.cidade());
        ponto.setCapacidadeTotalLitros(dto.capacidadeTotalLitros());
        PontoDistribuicao updated = repository.save(ponto);
        return new PontoDistribuicaoDTO(
                updated.getId(),
                updated.getNome(),
                updated.getEndereco(),
                updated.getCidade(),
                updated.getCapacidadeTotalLitros());
    }

    private PontoDistribuicao getPonto(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ponto de distribuição não encontrado"));
    }
}