package api.forum_duvida.domain.postagem.validation;

import api.forum_duvida.domain.ValidacaoException;
import api.forum_duvida.domain.postagem.DadosPostagem;
import api.forum_duvida.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorTituloMensagemRepetidasDoMesmoUsuario implements ValidadorPostagem {

    @Autowired
    private PostagemRepository postagemRepository;

    @Override
    public void validador(DadosPostagem dados) {
       if (!isValid(dados)){
            throw new ValidacaoException("não é permitido topicos repetidos");
       }
    }

    public Boolean isValid(DadosPostagem dados){
           return !postagemRepository.existsByTituloAndMensagemAndAutorId(dados.titulo(), dados.mensagem(), dados.id_autor());
    }
}
