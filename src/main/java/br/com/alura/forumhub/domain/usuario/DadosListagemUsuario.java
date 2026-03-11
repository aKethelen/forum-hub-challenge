package br.com.alura.forumhub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosListagemUsuario(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha
) {}