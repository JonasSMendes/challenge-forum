package api.forum_duvida.domain.postagem;

import api.forum_duvida.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "Postagem")
@Table(name = "postagens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String titulo;
    @NotBlank
    private String mensagem;
    @CreationTimestamp
    private LocalDateTime data;
    private Boolean ativo = true;
    @Embedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario autor;
    @NotBlank
    private String curso;


    public Postagem(String titulo, String mensagem, Usuario usuario, String curso) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.autor = usuario;
        this.curso = curso;
    }
}
