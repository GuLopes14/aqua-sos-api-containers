package br.com.aquasos.model.dto;

import br.com.aquasos.model.enums.NivelUrgencia;

public record PedidoAguaDTO(
        Long id,
        Long usuarioId,
        Integer quantidadeLitros,
        NivelUrgencia nivelUrgencia,
        String endereco
) {
}
