package br.com.aquasos.model.dto;

import java.time.LocalDate;

import br.com.aquasos.model.enums.StatusPedido;

public record PedidoAguaDTO(
        Long id,
        Long usuarioId,
        Long pontoDistribuicaoId,
        Integer quantidadeLitros,
        LocalDate dataSolicitacao,
        StatusPedido status
) {
}
