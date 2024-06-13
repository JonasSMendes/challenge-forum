package api.forum_duvida.domain.postagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosPostagem(
        Long id,
        @NotBlank(message = "O titulo nao pode estar vazio")
        String titulo,
        @NotBlank(message = "A mensagem nao pode estar vazia")
        String mensagem,
        @NotBlank(message = "O curso não pode estar vazio")
        String curso
) {

        public DadosPostagem(Postagem postagem){
                this(postagem.getId(), postagem.getTitulo(), postagem.getMensagem(),postagem.getCurso());
        }
}
