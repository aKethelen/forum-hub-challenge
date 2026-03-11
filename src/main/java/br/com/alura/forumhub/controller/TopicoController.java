package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.domain.curso.CursoRepository;
import br.com.alura.forumhub.domain.topico.*;
import br.com.alura.forumhub.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.tags.Tag; 
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/topicos")
@Tag(name = "4. Tópicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Object> cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriBuilder) {
        if (topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.badRequest().body("Já existe um tópico com este título e mensagem.");
        }

        var autor = usuarioRepository.findById(dados.idAutor())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com o ID: " + dados.idAutor()));

        var curso = cursoRepository.findById(dados.idCurso())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o ID: " + dados.idCurso()));

        var topico = new Topico(null, dados.titulo(), dados.mensagem(), LocalDateTime.now(), StatusTopico.NAO_RESPONDIDO, autor, curso);
        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @RequestParam(required = false) String nomeCurso,
            @RequestParam(required = false) Integer ano,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = org.springframework.data.domain.Sort.Direction.ASC) Pageable paginacao) {

        Page<Topico> pagina;
        if (nomeCurso != null && ano != null) {
            pagina = topicoRepository.findAllByCursoNomeAndAno(nomeCurso, ano, paginacao);
        } else if (nomeCurso != null) {
            pagina = topicoRepository.findAllByCursoNome(nomeCurso, paginacao);
        } else {
            pagina = topicoRepository.findAll(paginacao);
        }

        return ResponseEntity.ok(pagina.map(DadosListagemTopico::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {
        var topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DadosDetalhamentoTopico(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopico dados) {
        var topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Topico topico = topicoOptional.get();
        topico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        var topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}