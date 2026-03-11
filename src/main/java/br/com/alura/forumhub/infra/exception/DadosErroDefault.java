package br.com.alura.forumhub.infra.exception;

import java.time.LocalDateTime;

public record DadosErroDefault(
        String timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
    public DadosErroDefault(Integer status, String error, String message, String path) {
        this(LocalDateTime.now().toString(), status, error, message, path);
    }
}