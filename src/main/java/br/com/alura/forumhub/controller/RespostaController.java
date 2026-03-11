package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.domain.resposta.*;
import br.com.alura.forumhub.domain.topico.TopicoRepository;
import br.com.alura.forumhub.domain.topico.StatusTopico;
import br.com.alura.forumhub.domain.usuario.UsuarioRepository;
import br.com.alura.forumhub.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag; 
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/respostas")
@Tag(name = "5. Respostas")
public class RespostaController {

    @Autowired
    private RespostaRepository repository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoResposta> cadastrar(@RequestBody @Valid DadosCadastroResposta dados, UriComponentsBuilder uriBuilder) {
        var topico = topicoRepository.findById(dados.idTopico())
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        var autor = usuarioRepository.findById(dados.idAutor())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var resposta = new Resposta(dados, topico, autor);
        repository.save(resposta);

        var uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoResposta(resposta));
    }

    @PutMapping("/{id}/solucao")
    @Transactional
    public ResponseEntity<Object> marcarSolucao(@PathVariable Long id) {
        var resposta = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Resposta não encontrada"));

        var topico = resposta.getTopico();
        var usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!topico.getAutor().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas o autor do tópico pode marcar uma resposta como solução.");
        }

        if (resposta.getSolucao()) {
            return ResponseEntity.badRequest().body("Esta resposta já foi marcada como a solução.");
        }

        if (topico.getStatus() == StatusTopico.SOLUCIONADO) {
            return ResponseEntity.badRequest().body("Este tópico já possui uma solução marcada.");
        }

        resposta.marcarComoSolucao();

        return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
    }
}