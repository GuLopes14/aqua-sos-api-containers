package br.com.aquasos.model.dto;

public record PontoDistribuicaoDTO(
        Long id,
        String nome,
        String endereco,
        String cidade,
        Integer capacidadeTotalLitros
) {
}
