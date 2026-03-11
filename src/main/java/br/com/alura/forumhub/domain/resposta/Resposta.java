package br.com.alura.forumhub.domain.resposta;

import br.com.alura.forumhub.domain.topico.Topico;
import br.com.alura.forumhub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respostas")
@Entity(name = "Resposta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private Boolean solucao = false;

    public Resposta(DadosCadastroResposta dados, Topico topico, Usuario autor) {
        this.mensagem = dados.mensagem();
        this.topico = topico;
        this.autor = autor;
        this.dataCriacao = LocalDateTime.now();
        this.solucao = false;
    }

    public void marcarComoSolucao() {
        this.solucao = true;
        this.topico.marcarComoSolucionado(); // Este método precisa existir na classe Topico
    }
}