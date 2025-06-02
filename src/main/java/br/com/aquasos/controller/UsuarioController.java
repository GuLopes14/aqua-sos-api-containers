package br.com.aquasos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import br.com.aquasos.model.Usuario;
import br.com.aquasos.model.dto.RespostaUsuarioDTO;
import br.com.aquasos.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/registrar")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags = "Usuario", summary = "Cadastrar usuário", description = "Cadastra um novo usuário", responses = @ApiResponse(responseCode = "400", description = "Validação falhou"))
    @CacheEvict(value = "usuarios", allEntries = true)
    public RespostaUsuarioDTO create(@RequestBody @Valid Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        var userSaved = repository.save(usuario);
        return new RespostaUsuarioDTO(userSaved.getId(), userSaved.getNome(), userSaved.getEmail(),
                userSaved.getRole());
    }

    @GetMapping
    @Operation(tags = "Usuario", summary = "Listar usuários", description = "Devolve a lista de usuários com filtros e paginação")
    public Page<RespostaUsuarioDTO> index(Pageable pageable) {
        var usuarios = repository.findAll(pageable);
        return usuarios.map(u -> new RespostaUsuarioDTO(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getRole()));
    }

    @PutMapping("/{id}")
    @Operation(tags = "Usuario", summary = "Atualizar usuário", description = "Atualiza dados do usuário")
    @CacheEvict(value = "usuarios", allEntries = true)
    public RespostaUsuarioDTO update(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        if (usuarioAtualizado.getPassword() != null && !usuarioAtualizado.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioAtualizado.getPassword()));
        }
        var userSaved = repository.save(usuario);
        return new RespostaUsuarioDTO(userSaved.getId(), userSaved.getNome(), userSaved.getEmail(),
                userSaved.getRole());
    }

    @DeleteMapping("/{id}")
    @Operation(tags = "Usuario", summary = "Apagar usuário", description = "Deleta o usuário do sistema")
    @CacheEvict(value = "usuarios", allEntries = true)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        repository.deleteById(id);
    }
}