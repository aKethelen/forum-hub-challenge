package br.com.alura.forumhub.infra.status;

public record StatusResponse(
        String message,
        String version,
        String status,
        String documentation,
        String timestamp
) {}