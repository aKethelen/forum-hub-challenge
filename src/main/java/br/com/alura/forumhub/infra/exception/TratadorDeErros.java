package br.com.alura.forumhub.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DadosErroDefault> tratarErro404(HttpServletRequest request) {
        var erro = new DadosErroDefault(404, "Not Found", "O recurso solicitado não foi encontrado.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<DadosErroDefault> tratarErroRotaInexistente(NoHandlerFoundException ex, HttpServletRequest request) {
        var erro = new DadosErroDefault(
                404,
                "Not Found",
                "A rota '" + ex.getRequestURL() + "' não foi encontrada no servidor.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DadosErroDefault> tratarErro400(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var camposComErro = ex.getFieldErrors().stream().map(DadosErroValidacao::new).toList();
        var erro = new DadosErroDefault(400, "Bad Request", "Dados de formulário inválidos: " + camposComErro, request.getRequestURI());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DadosErroDefault> tratarErroAcessoNegado(HttpServletRequest request) {
        var erro = new DadosErroDefault(403, "Forbidden", "Você não tem permissão para acessar este recurso.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DadosErroDefault> tratarErro500(Exception ex, HttpServletRequest request) {
        var erro = new DadosErroDefault(500, "Internal Server Error", ex.getLocalizedMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<DadosErroDefault> tratarErroIntegridade(org.springframework.dao.DataIntegrityViolationException ex, HttpServletRequest request) {
        var erro = new DadosErroDefault(
                400,
                "Bad Request",
                "Violação de integridade de dados. Verifique se campos únicos (como e-mail ou título) já existem.",
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(erro);
    }
}