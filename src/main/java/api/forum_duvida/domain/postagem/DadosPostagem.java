package api.forum_duvida.domain.postagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosPostagem(
        @NotNull
        Long id,
        @NotBlank(message = "O titulo nao pode estar vazio")
        String titulo,
        @NotBlank(message = "A mensagem nao pode estar vazia")
        String mensagem,
        @NotNull(message = "precisa do autor")
        Long id_autor,
        @NotBlank(message = "O curso não pode estar vazio")
        String curso
) {

        public DadosPostagem(Postagem postagem){
                this(postagem.getId(), postagem.getTitulo(), postagem.getMensagem(),postagem.getAutor().getId(), postagem.getCurso());
        }
}
