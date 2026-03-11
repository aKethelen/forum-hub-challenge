package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.infra.status.StatusResponse;
import io.swagger.v3.oas.annotations.Operation; 
import io.swagger.v3.oas.annotations.tags.Tag; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@RestController
@Tag(name = "1. API Status")
public class StatusController {

    @GetMapping("/")
    @Operation(hidden = true, summary = "")
    public RedirectView home() {
        // Redireciona a raiz para o status
        return new RedirectView("/status");
    }

    @GetMapping("/docs")
    @Operation(summary = "Redireciona para a documentação Swagger UI", hidden = false)
    public RedirectView docs() {
        // Redireciona para o Swagger (ajuste o caminho se necessário)
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping("/status")
    @Operation(summary = "Verifica o status e integridade da aplicação", hidden = false)
    public StatusResponse status() {
        return new StatusResponse(
                "ForumHub API is running!",
                "1.0.0",
                "OK",
                "/docs",
                LocalDateTime.now().toString()
        );
    }
}