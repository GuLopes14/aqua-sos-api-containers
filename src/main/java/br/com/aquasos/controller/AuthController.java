package br.com.aquasos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.aquasos.model.Usuario;
import br.com.aquasos.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class AuthController {

    public record Token(String token, String email, String nome) {
    }

    public record Credentials(String email, String password) {
    }

    @Autowired
    private TokenService tokenService;

    @Autowired
    AuthenticationManager authManager;

    @PostMapping("/login")
    @Operation(security = @SecurityRequirement(name = ""), tags = "Autenticação", summary = "Realiza o login do usuário", description = "Endpoint para autenticar um usuário e gerar um token JWT")
    public ResponseEntity<?> login(@RequestBody Credentials credentials) {
        var authentication = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());
        var user = (Usuario) authManager.authenticate(authentication).getPrincipal();

        Token tokenObj = tokenService.createToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", tokenObj.token());
        response.put("nome", user.getNome());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());
        response.put("id", user.getId());

        return ResponseEntity.ok(response);
    }
}
