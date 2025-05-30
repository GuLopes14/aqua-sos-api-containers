package br.com.aquasos.model.dto;

import br.com.aquasos.model.enums.UserRole;

public record RespostaUsuarioDTO(
    Long id,
    String nome,
    String email,
    UserRole role
) {}
