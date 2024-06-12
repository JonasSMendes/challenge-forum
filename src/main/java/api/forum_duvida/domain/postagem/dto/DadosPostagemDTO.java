package api.forum_duvida.domain.postagem.dto;

import api.forum_duvida.domain.postagem.Postagem;

import java.time.LocalDateTime;

public record DadosPostagemDTO(String titulo,
                               String mensagem,
                               Long id_autor,
                               String curso,
                               LocalDateTime data,
                               Boolean ativo) {

    public DadosPostagemDTO(Postagem postagem){
        this(postagem.getTitulo(), postagem.getMensagem(), postagem.getAutor().getId(), postagem.getCurso(), postagem.getData(),postagem.getAtivo());
    }
}
