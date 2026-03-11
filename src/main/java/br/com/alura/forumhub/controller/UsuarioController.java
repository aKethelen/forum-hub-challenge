package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.domain.usuario.*;
import io.swagger.v3.oas.annotations.tags.Tag; 
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "3. Usuários")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemUsuario> cadastrar(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder,
            jakarta.servlet.http.HttpServletRequest request) {

        if (repository.existsByEmail(dados.email())) {
            return ResponseEntity.badRequest().body(null);
        }

        var senhaCriptografada = passwordEncoder.encode(dados.senha());

        var usuario = new Usuario(null, dados.nome(), dados.email(), senhaCriptografada);
        repository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity.created(uri)
                .body(new DadosListagemUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {

        var pagina = repository.findAll(paginacao)
                .map(DadosListagemUsuario::new);

        return ResponseEntity.ok(pagina);
    }
}