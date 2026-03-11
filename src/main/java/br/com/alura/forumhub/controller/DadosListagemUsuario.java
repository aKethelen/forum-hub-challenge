package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.domain.usuario.Usuario;

public record DadosListagemUsuario(
        Long id,
        String nome,
        String email
) {

    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

}