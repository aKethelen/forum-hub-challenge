package br.com.alura.forumhub.domain.resposta;

import java.time.LocalDateTime;

public record DadosDetalhamentoResposta(Long id, String mensagem, Long idTopico, LocalDateTime dataCriacao, String autor, Boolean solucao) {
    public DadosDetalhamentoResposta(Resposta resposta) {
        this(resposta.getId(), resposta.getMensagem(), resposta.getTopico().getId(), resposta.getDataCriacao(), resposta.getAutor().getNome(), resposta.getSolucao());
    }
}