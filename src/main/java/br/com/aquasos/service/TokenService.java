package br.com.aquasos.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.aquasos.controller.AuthController.Token;
import br.com.aquasos.model.Usuario;
import br.com.aquasos.model.enums.UserRole;

@Service
public class TokenService {

    Instant expiresAt = LocalDateTime.now().plusMinutes(120).toInstant(ZoneOffset.ofHours(-3));
    Algorithm algorithm = Algorithm.HMAC256("secret");

    public Token createToken(Usuario usuario) {
        var jwt = JWT.create()
                .withSubject(usuario.getId().toString())
                .withClaim("email", usuario.getEmail())
                .withClaim("role", usuario.getRole().toString())
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new Token(jwt, usuario.getEmail());
    }

    public Usuario getUsuarioFromToken(String token) {
        var verifiedToken = JWT.require(algorithm).build().verify(token);
        return Usuario.builder()
                .id(Long.valueOf(verifiedToken.getSubject()))
                .email(verifiedToken.getClaim("email").toString())
                .role(UserRole.valueOf(verifiedToken.getClaim("role").asString()))
                .build();
    }
}