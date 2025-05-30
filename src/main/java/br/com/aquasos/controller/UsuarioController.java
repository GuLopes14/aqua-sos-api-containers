package br.com.aquasos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.aquasos.model.Usuario;
import br.com.aquasos.model.dto.RespostaUsuarioDTO;
import br.com.aquasos.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        tags = "Usuario",
        summary = "Cadastrar usuário",
        description = "Cadastra um novo usuário",
        responses = @ApiResponse(responseCode = "400", description = "Validação falhou")
    )
    @CacheEvict(value = "usuarios", allEntries = true)
    public RespostaUsuarioDTO create(@RequestBody @Valid Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        var userSaved = repository.save(usuario);
        return new RespostaUsuarioDTO(userSaved.getId(), userSaved.getNome(), userSaved.getEmail(), userSaved.getRole());
    }
}